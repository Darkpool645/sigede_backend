package mx.edu.utez.sigede_backend.controllers.Institutions.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.institution.InstitutionStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionResponseDTO {
    private Long institutionId;
    private String name;
    private InstitutionStatus institutionStatus;
}
