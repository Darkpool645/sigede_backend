package mx.edu.utez.sigede_backend.controllers.Institutions.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import mx.edu.utez.sigede_backend.models.institution.InstitutionStatus;

public class InstitutionUpdateDTO {

    @NotNull(message = "El ID de la institución es obligatorio")
    private Long institutionId;

    @Size(max = 255, message = "El nombre no puede tener más de 255 caracteres")
    private String name;

    @Size(max = 255, message = "La dirección no puede tener más de 255 caracteres")
    private String address;

    @Size(max = 30, message = "El teléfono de contacto no puede tener más de 30 caracteres")
    private String phoneContact;

    private InstitutionStatus institutionStatus;

    @Size(max = 500, message = "El logo no puede tener más de 500 caracteres")
    private String logo;

    // Getters y Setters
    public Long getInstitutionId() {
        return institutionId;
    }

    public void setInstitutionId(Long institutionId) {
        this.institutionId = institutionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneContact() {
        return phoneContact;
    }

    public void setPhoneContact(String phoneContact) {
        this.phoneContact = phoneContact;
    }

    public InstitutionStatus getInstitutionStatus() {
        return institutionStatus;
    }

    public void setInstitutionStatus(InstitutionStatus institutionStatus) {
        this.institutionStatus = institutionStatus;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}