package mx.edu.utez.sigede_backend.controllers.credentials.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestByInstitution {
    @NotNull(message = "institution.id.notnull")
    private Long institutionId;
    private String fullname;
}
