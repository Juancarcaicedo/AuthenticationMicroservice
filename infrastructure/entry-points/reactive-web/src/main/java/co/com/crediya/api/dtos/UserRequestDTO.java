package co.com.crediya.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    @Schema(description = "Nombre del usuario", example = "Juan")
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    @NotBlank(message = "El apellido es obligatorio")
    private String lastname;

    @Schema(description = "Fecha de nacimiento", example = "1990-01-01")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate birthDate;

    @Schema(description = "Dirección de residencia", example = "Calle 123")
    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @Schema(description = "Número de teléfono", example = "3001234567")
    @NotBlank(message = "El teléfono es obligatorio")
    private String telephone;

    @Schema(description = "Correo electrónico", example = "juan@email.com")
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    private String email;

    @Schema(description = "Número de documento de identidad", example = "123456789")
    @NotBlank(message = "El documento es obligatorio")
    private String document;

    @Schema(description = "Salario base mensual", example = "12000000")
    @NotNull(message = "El salario base es obligatorio")
    @DecimalMin(value = "0.0", message = "El salario debe ser mayor o igual a 0")
    @DecimalMax(value = "15000000.0", message = "El salario no puede superar los 15 millones")
    private BigDecimal baseSalary;


}
