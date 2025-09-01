package co.com.crediya.usecase.finduserbydocument;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserGateway;
import exceptions.BusinessException;
import exceptions.BusinessRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class FindUserByDocumentUseCaseTest
{
    private UserGateway userGateway;
    private FindUserByDocumentUseCase useCase;

    @BeforeEach
    void setUp() {
        userGateway = mock(UserGateway.class);
        useCase = new FindUserByDocumentUseCase(userGateway);
    }

    @Test
    void shouldReturnUserWhenDocumentExists() {
        var document = "1234567890";
        var user = User.builder()
                .document(document)
                .email("test@correo.com")
                .name("Juan")
                .build();

        when(userGateway.findByDocument(document)).thenReturn(Mono.just(user));
        StepVerifier.create(useCase.execute(document))
                .expectNext(user)
                .verifyComplete();

        verify(userGateway).findByDocument(document);
    }

    @Test
    void shouldThrowBusinessExceptionWhenUserNotFound() {
        var document = "0000000000";
        when(userGateway.findByDocument(document)).thenReturn(Mono.empty());
        StepVerifier.create(useCase.execute(document))
                .expectErrorMatches(error ->
                        error instanceof BusinessException &&
                                ((BusinessException) error).getRule() == BusinessRule.USER_NOT_FOUND
                )
                .verify();

        verify(userGateway).findByDocument(document);
    }
}
