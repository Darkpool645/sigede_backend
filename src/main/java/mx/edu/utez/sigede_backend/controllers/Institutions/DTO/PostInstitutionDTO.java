package mx.edu.utez.sigede_backend.controllers.Institutions.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostInstitutionDTO {
    @NotBlank(message = "field.not.null")
    private String institutionName;
    @NotBlank(message = "field.not.null")
    private String institutionAddress;
    @NotBlank(message = "field.not.null")
    private String institutionEmail;
    @NotBlank(message = "field.not.null")
    private String institutionPhone;
    private String logo;
}
