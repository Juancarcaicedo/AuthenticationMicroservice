package co.com.crediya.r2dbc;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserGateway;
import co.com.crediya.r2dbc.entities.UserEntity;
import co.com.crediya.r2dbc.helper.ReactiveAdapterOperations;
import lombok.AllArgsConstructor;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@Repository
public class UserReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        User,                   // Modelo de dominio
        UserEntity,             // Entidad de infraestructura
        Long,                   // Tipo de ID
        UserReactiveRepository  // Repositorio Spring Data
        > implements UserGateway {

    private final UserReactiveRepository repository;
    private final ObjectMapper mapper;
    private final TransactionalOperator txOperator;

    public UserReactiveRepositoryAdapter(
            UserReactiveRepository repository,
            ObjectMapper mapper,
            TransactionalOperator txOperator
    ) {
        super(repository, mapper, entity -> mapper.map(entity, User.class));
        this.repository = repository;
        this.mapper = mapper;
        this.txOperator = txOperator;
    }

    @Override
    public Mono<User> save(User user) {
        UserEntity entity = mapper.map(user, UserEntity.class);
        return repository.save(entity)
                .map(saved -> mapper.map(saved, User.class))
                .as(txOperator::transactional);
    }

    @Override
    public Mono<User> findByName(String name) {
        return repository.findByName(name)
                .map(entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity -> mapper.map(entity, User.class));
    }

    @Override
    public Mono<User> findByDocument(String document) {
        return repository.findByDocument(document)
                .map(entity -> mapper.map(entity, User.class));
    }

    @Override
    public Flux<User> findAll() {
        return repository.findAll()
                .map(entity -> mapper.map(entity, User.class));
    }
}



