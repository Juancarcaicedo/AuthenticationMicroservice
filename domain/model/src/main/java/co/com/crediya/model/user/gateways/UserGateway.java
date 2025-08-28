package co.com.crediya.model.user.gateways;

import co.com.crediya.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserGateway {
    Mono<User> save(User user);
    Mono<User> findByName(String username);
    Mono<User> findByEmail(String email);
    Mono<User> findByDocument(String document);
    Flux<User> findAll();
}
