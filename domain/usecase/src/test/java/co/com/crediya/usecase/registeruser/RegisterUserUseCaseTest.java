package co.com.crediya.usecase.registeruser;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserGateway;
import exceptions.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegisterUserUseCaseTest {

    @Mock(lenient = true)
    private UserGateway userGateway;

    @InjectMocks
    private RegisterUserUseCase useCase;

    private final User user = new User(
            null,
            "Juan",
            "Pérez",
            "Calle 123",
            LocalDate.of(1990, 1, 1),
            "juan@email.com",
            "123456789",
            "3001234567",
            new BigDecimal("12000000"),
            1
    );
// camino feliz todo pasa en esta prueba
    @Test
    void shouldRegisterUserSuccessfully() {
        when(userGateway.findByEmail(user.getEmail())).thenReturn(Mono.empty()); // simulamos que el correo  no lo tenga otra persona
        when(userGateway.findByDocument(user.getDocument())).thenReturn(Mono.empty());// simulamos que el documento no lo tenga otra persona
        when(userGateway.save(user)).thenReturn(Mono.just(user));// simulamos que se guardo exitosamente

        StepVerifier.create(useCase.register(user)) // inicia la verificación del flujo reactivo.
                .expectNext(user) // espera que el flujo emita el usuario como resultado.
                .verifyComplete(); //espera que el flujo termine sin errores.
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        when(userGateway.findByEmail(user.getEmail())).thenReturn(Mono.just(user));// simulamos que encontramos un correo registrado
        StepVerifier.create(useCase.register(user))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().equals("El correo ya está registrado")) // lanzamos la excepcion personalizada creada
                .verify();
    }
// problema con este test esta devolviendo null
    @Test
    void shouldFailWhenDocumentAlreadyExists() {
        when(userGateway.findByEmail(user.getEmail())).thenReturn(Mono.empty()); //
        when(userGateway.findByDocument(user.getDocument())).thenReturn(Mono.just(user)); // ← documento ocupado
        StepVerifier.create(useCase.register(user))
                .expectErrorMatches(e -> e instanceof BusinessException &&
                        e.getMessage().equals("El documento ya está registrado"))
                .verify();
    }


}
