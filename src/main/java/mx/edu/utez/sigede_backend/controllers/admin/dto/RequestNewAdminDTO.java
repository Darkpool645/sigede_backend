package mx.edu.utez.sigede_backend.controllers.admin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestNewAdminDTO {
    @NotBlank(message = "capturer.name.notnull")
    private String name;
    @NotBlank(message = "user.email.notnull")
    @Email(message = "user.email.invalid")
    private String email;
    @NotNull(message = "institution.id.notnull")
    private Long fkInstitution;

}
