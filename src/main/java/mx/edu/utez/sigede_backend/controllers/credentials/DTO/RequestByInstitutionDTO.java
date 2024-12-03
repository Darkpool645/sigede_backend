package mx.edu.utez.sigede_backend.controllers.credentials.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestByInstitutionDTO {
    @NotNull(message = "institution.id.notnull")
    private Long institutionId;
}
