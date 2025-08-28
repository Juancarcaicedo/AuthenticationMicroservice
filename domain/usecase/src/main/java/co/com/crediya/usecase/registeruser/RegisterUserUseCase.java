package co.com.crediya.usecase.registeruser;

import exceptions.BusinessException;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterUserUseCase {
    private  final UserGateway userGateway;
    public Mono<User> register(User user) {
        return userGateway.findByEmail(user.getEmail())
                .flatMap(u -> Mono.<User>error(new BusinessException("El correo ya está registrado")))
                .switchIfEmpty(Mono.defer(() ->
                        userGateway.findByDocument(user.getDocument())
                                .flatMap(u -> Mono.<User>error(new BusinessException("El documento ya está registrado")))
                                .switchIfEmpty(Mono.defer(() -> userGateway.save(user)))
                ));
    }


}


