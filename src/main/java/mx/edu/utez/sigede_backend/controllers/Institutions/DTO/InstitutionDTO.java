package mx.edu.utez.sigede_backend.controllers.Institutions.DTO;

import java.util.UUID;

public class InstitutionDTO {
    private UUID institutionId;
    private String name;
    private String logo;

    public InstitutionDTO(UUID institutionId, String name, String logo) {
        this.institutionId = institutionId;
        this.name = name;
        this.logo = logo;
    }

    public UUID getInstitutionId() {
        return institutionId;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }
}
