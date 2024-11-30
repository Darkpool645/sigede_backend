package mx.edu.utez.sigede_backend.controllers.capturers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestGetCapturistsByNameDTO {
    @NotBlank(message = "field.not.null")
    private String name;
    @NotNull(message = "field.not.null")
    private int page;
    @NotNull(message = "field.not.null")
    private int size;
}
