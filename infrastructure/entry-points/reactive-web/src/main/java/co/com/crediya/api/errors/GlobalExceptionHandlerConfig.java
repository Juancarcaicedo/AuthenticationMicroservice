package co.com.crediya.api.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.*;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.*;

import static org.springframework.http.HttpStatus.*;
@Slf4j
@Configuration
public class GlobalExceptionHandlerConfig {
    @Bean
    @Order(-2)
    public ErrorWebExceptionHandler errorWebExceptionHandler(ObjectMapper mapper) {
        return (exchange, ex) -> {
            var req = exchange.getRequest();
            var res = exchange.getResponse();

            var mapped = mapException(ex);

            res.setStatusCode(mapped.status());
            res.getHeaders().setContentType(MediaType.APPLICATION_JSON);

            var correlationId = res.getHeaders().getFirst("X-Correlation-Id");
            var body = new ApiError(
                    mapped.code(),
                    mapped.message(),
                    mapped.details(),
                    correlationId != null ? correlationId : "",
                    OffsetDateTime.now(),
                    req.getPath().value()
            );

            byte[] json;
            try {
                json = mapper.writeValueAsBytes(body);
            } catch (Exception serializationError) {
                json = ("{\"codigo\":\"UNEXPECTED_ERROR\",\"mensaje\":\"Error serializando respuesta\"}")
                        .getBytes(StandardCharsets.UTF_8);
            }

            switch (mapped.level()) {
                case INFO -> log.info("[{}] {} {} -> {} {}", mapped.code(), req.getMethod(), req.getPath(), mapped.status().value(), mapped.message());
                case WARN -> log.warn("[{}] {} {} -> {} {}", mapped.code(), req.getMethod(), req.getPath(), mapped.status().value(), mapped.message());
                case ERROR -> log.error("[{}] {} {} -> {} {}", mapped.code(), req.getMethod(), req.getPath(), mapped.status().value(), mapped.message(), ex);
            }

            var buffer = res.bufferFactory().wrap(json);
            return res.writeWith(Mono.just(buffer));
        };
    }

    //  Mapeo de excepciones

    private Mapped mapException(Throwable ex) {
        if (ex instanceof BusinessException be) {
            return switch (be.getRule()) {
                case EMAIL_ALREADY_EXISTS -> new Mapped(CONFLICT, "EMAIL_ALREADY_EXISTS", be.getMessage(), List.of(), Level.WARN);
                case DOCUMENT_ALREADY_EXISTS -> new Mapped(CONFLICT, "DOCUMENT_ALREADY_EXISTS", be.getMessage(), List.of(), Level.WARN);
            };
        }

        if (ex instanceof IllegalArgumentException || ex instanceof ServerWebInputException) {
            return new Mapped(BAD_REQUEST, "VALIDATION_ERROR", "La solicitud es inválida", details(ex), Level.WARN);
        }

        if (ex instanceof WebExchangeBindException bind) {
            var d = bind.getFieldErrors().stream()
                    .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                    .toList();
            return new Mapped(BAD_REQUEST, "VALIDATION_ERROR", "La solicitud es inválida", d, Level.WARN);
        }

        if (ex instanceof MethodNotAllowedException) {
            return new Mapped(METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "Método no permitido", details(ex), Level.INFO);
        }

        if (ex instanceof UnsupportedMediaTypeStatusException) {
            return new Mapped(UNSUPPORTED_MEDIA_TYPE, "UNSUPPORTED_MEDIA", "Tipo de contenido no soportado", details(ex), Level.WARN);
        }

        if (ex instanceof ResponseStatusException rse) {
            var status = HttpStatus.valueOf(rse.getStatusCode().value());
            var code = (status == NOT_FOUND) ? "NOT_FOUND" : "UNEXPECTED_ERROR";
            var message = rse.getReason() != null ? rse.getReason() : (status == NOT_FOUND ? "Recurso no encontrado" : "Error inesperado");
            return new Mapped(status, code, message, details(ex), status.is4xxClientError() ? Level.WARN : Level.ERROR);
        }

        return new Mapped(INTERNAL_SERVER_ERROR, "UNEXPECTED_ERROR", "Ocurrió un error inesperado", List.of(), Level.ERROR);
    }

    private List<String> details(Throwable ex) {
        if (ex != null && ex.getMessage() != null && !ex.getMessage().isBlank()) {
            return List.of(ex.getMessage());
        }
        return List.of();
    }

    private record Mapped(HttpStatus status, String code, String message, List<String> details, Level level) {}
    private enum Level { INFO, WARN, ERROR }

    public record ApiError(
            String codigo,
            String mensaje,
            List<String> detalles,
            String correlationId,
            OffsetDateTime timestamp,
            String path
    ) {}
}
