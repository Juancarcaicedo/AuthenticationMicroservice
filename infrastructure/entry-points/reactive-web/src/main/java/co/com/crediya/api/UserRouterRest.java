package co.com.crediya.api;

import co.com.crediya.api.dtos.UserRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Configuration
public class UserRouterRest {

    @Bean
    @RouterOperation(
            path = "/api/v1/users",
            method = RequestMethod.POST,
            beanClass = UserHandler.class,
            beanMethod = "registerUser",
            operation = @Operation(
                    operationId = "registerUser",
                    summary = "Registrar nuevo usuario",
                    tags = {"Usuarios"},
                    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                            description = "Datos del usuario a registrar",
                            required = true,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserRequestDTO.class)
                            )
                    ),
                    responses = {
                            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
                            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                            @ApiResponse(responseCode = "409", description = "Conflicto de negocio")
                    }
            )
    )
    public RouterFunction<ServerResponse> userRoutes(UserHandler handler) {
        return RouterFunctions.route(POST("/api/v1/users"), handler::registerUser);
    }
}
