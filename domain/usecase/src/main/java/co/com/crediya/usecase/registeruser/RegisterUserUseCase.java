package co.com.crediya.usecase.registeruser;

import exceptions.BusinessException;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserGateway;
import exceptions.BusinessRule;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserGateway userGateway;

    public Mono<User> register(User user) {
        return userGateway.findByEmail(user.getEmail())
                .flatMap(existing -> Mono.<User>error(new BusinessException(
                        BusinessRule.EMAIL_ALREADY_EXISTS
                )))
                .switchIfEmpty(Mono.defer(() ->
                        userGateway.findByDocument(user.getDocument())
                                .flatMap(existing -> Mono.<User>error(new BusinessException(
                                        BusinessRule.DOCUMENT_ALREADY_EXISTS
                                )))
                                .switchIfEmpty(userGateway.save(user))
                ));
    }
}



