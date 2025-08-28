package co.com.crediya.r2dbc.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class UserEntity {
    @Id
    @Column("user_id")
    private Long userId;

    @Column("name")
    private String name;

    @Column("lastname")
    private String lastname;

    @Column("address")
    private String address;

    @Column("birth_date")
    private LocalDate birthDate;

    @Column("email")
    private String email;

    @Column("document")
    private String document;

    @Column("telephone")
    private String telephone;

    @Column("base_salary")
    private BigDecimal baseSalary;

    @Column("role_id")
    private Integer roleId;
}
