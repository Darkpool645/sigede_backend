package mx.edu.utez.sigede_backend.controllers.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestGetByNameAndInstitutionDTO {
    private String name;
    @NotNull(message = "field.not.null")
    private Long institutionId;
    @NotNull(message = "field.not.null")
    private int page;
    @NotNull(message = "field.not.null")
    private int size;
}
