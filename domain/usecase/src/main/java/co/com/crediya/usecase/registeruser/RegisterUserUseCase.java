package co.com.crediya.usecase.registeruser;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterUserUseCase {
    private  final UserGateway userGateway;
    public Mono<User> register(User user) {
        return userGateway.findByEmail(user.getEmail())
                .flatMap(existing -> Mono.<User>error(new IllegalArgumentException("El correo ya está registrado")))
                .switchIfEmpty(
                        userGateway.findByDocument(user.getDocument())
                                .flatMap(existing -> Mono.<User>error(new IllegalArgumentException("El documento ya está registrado")))
                                .switchIfEmpty(userGateway.save(user))
                );
    }
}
