package co.com.crediya.api.mapper;

import co.com.crediya.api.dtos.UserRequestDTO;
import co.com.crediya.model.user.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toDomain(UserRequestDTO dto) {
        return new User(
                null,
                dto.getName(),
                dto.getLastname(),
                dto.getAddress(),
                dto.getBirthDate(),
                dto.getEmail(),
                dto.getDocument(),
                dto.getTelephone(),
                dto.getBaseSalary(),
                1
        );
    }
}
