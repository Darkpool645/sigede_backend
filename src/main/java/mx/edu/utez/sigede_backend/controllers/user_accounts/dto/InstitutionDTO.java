package mx.edu.utez.sigede_backend.controllers.user_accounts.dto;

import lombok.Data;
import mx.edu.utez.sigede_backend.models.institution.InstitutionStatus;

@Data
public class InstitutionDTO {
    private Long institutionId;
    private String name;
    private String address;
    private String phoneContact;
    private String emailContact;

    private String logo;

    public InstitutionDTO(Long institutionId,String name, String address, String phoneContact, String emailContact, String logo) {
        this.institutionId = institutionId;
        this.name = name;
        this.address =address;
        this.phoneContact = phoneContact;
        this.emailContact = emailContact;
        this.logo = logo;
    }
}
