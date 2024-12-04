package mx.edu.utez.sigede_backend.controllers.Institutions.DTO;

import lombok.Data;
import mx.edu.utez.sigede_backend.models.institution.InstitutionStatus;

@Data
public class ResponseInstitutionUpdateDTO {
    private Long institutionId;
    private String name;
    private String address;
    private String phoneContact;
    private InstitutionStatus institutionStatus;
    private String logo;
}