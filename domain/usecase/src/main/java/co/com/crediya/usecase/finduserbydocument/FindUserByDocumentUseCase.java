package co.com.crediya.usecase.finduserbydocument;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserGateway;
import exceptions.BusinessException;
import exceptions.BusinessRule;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindUserByDocumentUseCase {
    private final UserGateway userGateway;
    public Mono<User>execute(String document){
        return  userGateway.findByDocument(document)
                .switchIfEmpty(Mono.error(new BusinessException(BusinessRule.USER_NOT_FOUND)));

    }
}
