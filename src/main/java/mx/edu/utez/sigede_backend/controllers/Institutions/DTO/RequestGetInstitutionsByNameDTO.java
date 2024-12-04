package mx.edu.utez.sigede_backend.controllers.Institutions.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestGetInstitutionsByNameDTO {
    @NotBlank(message = "field.not.null")
    private String name;
    @NotNull(message = "field.not.null")
    private int page;
    @NotNull(message = "field.not.null")
    private int size;
}
