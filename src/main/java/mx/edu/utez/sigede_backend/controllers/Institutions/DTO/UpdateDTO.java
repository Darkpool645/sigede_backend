package mx.edu.utez.sigede_backend.controllers.Institutions.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.institution.InstitutionStatus;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDTO {
    private Long institutionId;
    private String institutionName;
    private String institutionAddress;
    private String institutionEmail;
    private String institutionPhone;
    private InstitutionStatus institutionStatus;
    private String logo;

}
