package co.com.crediya.api;

import co.com.crediya.api.dtos.UserRequestDTO;
import co.com.crediya.api.dtos.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Configuration
public class UserRouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/users",
                    method = RequestMethod.POST,
                    beanClass = UserHandler.class,
                    beanMethod = "registerUser",
                    operation = @Operation(
                            operationId = "registerUser",
                            summary = "Registrar nuevo usuario",
                            tags = {"Usuarios"},
                            requestBody = @RequestBody(
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
            ),
            @RouterOperation(
                    path = "/api/v1/users/document/{document}",
                    method = RequestMethod.GET,
                    beanClass = UserHandler.class,
                    beanMethod = "getUserByDocument",
                    operation = @Operation(
                            operationId = "getUserByDocument",
                            summary = "Consultar usuario por documento",
                            tags = {"Usuarios"},
                            parameters = {
                                    @Parameter(name = "document", description = "Documento del usuario", required = true)
                            },
                            responses = {
                                    @ApiResponse(responseCode = "200", description = "Usuario encontrado", content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
                                    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> userRoutes(UserHandler handler) {
        return RouterFunctions.route(POST("/api/v1/users"), handler::registerUser)
                .andRoute(GET("/api/v1/users/document/{document}"), handler::getUserByDocument);
    }
}
