package co.com.crediya.model.user;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long userId;
    private String name;
    private String lastname;
    private String address;
    private LocalDate birthDate;
    private String email;
    private String document;
    private String telephone;
    private BigDecimal baseSalary;
    private Integer roleId;
}
