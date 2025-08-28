package co.com.crediya.api.errors;

import Exceptions.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;

import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Component
public class ApiErrorHandler extends AbstractErrorWebExceptionHandler {

    public ApiErrorHandler(ErrorAttributes errorAttributes,
                           WebProperties webProperties,
                           ApplicationContext applicationContext,
                           ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, webProperties.getResources(), applicationContext);
        this.setMessageWriters(codecConfigurer.getWriters());
        this.setMessageReaders(codecConfigurer.getReaders());
    }

    private static ErrorProperties buildErrorProperties() {
        ErrorProperties props = new ErrorProperties();
        props.setIncludeMessage(ErrorProperties.IncludeAttribute.ALWAYS);
        props.setIncludeStacktrace(ErrorProperties.IncludeAttribute.NEVER);
        return props;
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
        Throwable error = getError(request);
        log.warn("Error capturado: {}", error.getMessage());
        log.debug("Stack trace completo", error);

        HttpStatus status = error instanceof BusinessException ? HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;

        Map<String, Object> response = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "path", request.path(),
                "status", status.value(),
                "error", error.getMessage()
        );

        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(response);
    }
}