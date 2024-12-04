package mx.edu.utez.sigede_backend.controllers.Institutions.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.institution.InstitutionStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseInstitutionInfoDTO {
    private Long institutionId;
    private String name;
    private String address;
    private String email_contact;
    private String phoneContact;
    private String logo;
    private InstitutionStatus institutionStatus;
}