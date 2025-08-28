package co.com.crediya.api;

import co.com.crediya.api.dtos.UserRequestDTO;
import co.com.crediya.api.mapper.UserMapper;
import co.com.crediya.usecase.registeruser.RegisterUserUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserHandler {
    private final RegisterUserUseCase registerUserUseCase;
    private final UserMapper userMapper;
    private final Validator validator;
    private  static  final Logger log =  LoggerFactory.getLogger(UserHandler.class);
    public Mono<ServerResponse> registerUser(ServerRequest request) {
        log.info("Iniciando Registro De Usuario");
        return request.bodyToMono(UserRequestDTO.class)
                .doOnNext(dto -> log.debug("Dto Recibido:{}",dto))
                .flatMap(dto -> {
                    Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
                    if (!violations.isEmpty()) {
                        String errorMessage = violations.iterator().next().getMessage();
                        log.warn("Fallo al validar:{}", errorMessage);
                        return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(errorMessage);
                    }
                    return registerUserUseCase.register(userMapper.toDomain(dto))
                            .doOnNext(user -> log.info("Usuario registrado con ID: {}", user.getUserId()))
                            .map(userMapper::toResponse)
                            .flatMap(response -> {
                                log.debug("Respuesta enviada: {}", response);
                                return ServerResponse.status(HttpStatus.CREATED).bodyValue(response);
                            })
                            .onErrorResume(e -> {
                                log.error("Error en el registro de usuario", e);
                                return ServerResponse.status(HttpStatus.BAD_REQUEST).bodyValue(e.getMessage());
                            });

                });
    }
}
