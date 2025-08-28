package co.com.crediya.r2dbc;

import co.com.crediya.r2dbc.entities.UserEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;


public interface UserReactiveRepository extends ReactiveCrudRepository<UserEntity, Long>, ReactiveQueryByExampleExecutor<UserEntity> {
    Mono<UserEntity> findByName(String name);
    Mono<UserEntity> findByEmail(String email);
    Mono<UserEntity> findByDocument(String document);
}
