package co.com.crediya.r2dbc;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserGateway;
import co.com.crediya.r2dbc.entities.UserEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import lombok.AllArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@AllArgsConstructor
@Repository
public class UserReactiveRepositoryAdapter implements UserGateway{
    private final  UserReactiveRepository repository;

    @Override
    public Mono<User> save(User user) {
        UserEntity entity = new UserEntity(
                null,
                user.getName(),
                user.getLastname(),
                user.getAddress(),
                user.getBirthDate(),
                user.getEmail(),
                user.getDocument(),
                user.getTelephone(),
                user.getBaseSalary(),
                user.getRoleId()
        );

        return repository.save(entity)
                .map(saved -> new User(
                        saved.getUserId(),
                        saved.getName(),
                        saved.getLastname(),
                        saved.getAddress(),
                        saved.getBirthDate(),
                        saved.getEmail(),
                        saved.getDocument(),
                        saved.getTelephone(),
                        saved.getBaseSalary(),
                        saved.getRoleId()
                ));
    }

    @Override
    public Mono<User> findByName(String name) {
        return repository.findByName(name)
                .map(entity -> new User(
                        entity.getUserId(),
                        entity.getName(),
                        entity.getLastname(),
                        entity.getAddress(),
                        entity.getBirthDate(),
                        entity.getEmail(),
                        entity.getDocument(),
                        entity.getTelephone(),
                        entity.getBaseSalary(),
                        entity.getRoleId()
                ));
    }
    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity -> new User(
                        entity.getUserId(),
                        entity.getName(),
                        entity.getLastname(),
                        entity.getAddress(),
                        entity.getBirthDate(),
                        entity.getEmail(),
                        entity.getDocument(),
                        entity.getTelephone(),
                        entity.getBaseSalary(),
                        entity.getRoleId()
                ));
    }

    @Override
    public Mono<User> findByDocument(String document) {
        return repository.findByDocument(document)
                .map(entity -> new User(
                        entity.getUserId(),
                        entity.getName(),
                        entity.getLastname(),
                        entity.getAddress(),
                        entity.getBirthDate(),
                        entity.getEmail(),
                        entity.getDocument(),
                        entity.getTelephone(),
                        entity.getBaseSalary(),
                        entity.getRoleId()
                ));
    }

    @Override
    public Flux<User> findAll() {
        return repository.findAll()
                .map(entity -> new User(
                        entity.getUserId(),
                        entity.getName(),
                        entity.getLastname(),
                        entity.getAddress(),
                        entity.getBirthDate(),
                        entity.getEmail(),
                        entity.getDocument(),
                        entity.getTelephone(),
                        entity.getBaseSalary(),
                        entity.getRoleId()
                ));
    }
}

