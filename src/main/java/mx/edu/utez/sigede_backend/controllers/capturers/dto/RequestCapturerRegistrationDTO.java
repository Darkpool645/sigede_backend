package mx.edu.utez.sigede_backend.controllers.capturers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.institution.Institution;


@Data
@NoArgsConstructor
public class RequestCapturerRegistrationDTO {
    @NotBlank(message = "capturer.name.notnull")
    private String name;
    @NotBlank(message = "user.email.notnull")
    @Email(message = "user.email.invalid")
    private String email;
    @NotBlank(message = "capturer.password.notnull")
    private String password;
    @NotNull(message = "institution.id.notnull")
    private Long fkInstitution;
}
