package mx.edu.utez.sigede_backend.controllers.user_accounts.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestAllAdminByInstitutionDTO {
    @NotNull(message = "rol.required")
    private String role;
    @NotNull(message = "institution.id.notnull")
    private Long institutionId;
    String name;
}
