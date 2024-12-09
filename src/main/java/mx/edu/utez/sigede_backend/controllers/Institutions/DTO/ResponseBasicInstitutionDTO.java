package mx.edu.utez.sigede_backend.controllers.Institutions.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseBasicInstitutionDTO {
    private Long institutionId;
    private String name;
    private String email_contact;
    private String logo;
}
