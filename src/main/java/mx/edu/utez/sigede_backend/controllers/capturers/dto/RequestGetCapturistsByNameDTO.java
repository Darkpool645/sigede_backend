package mx.edu.utez.sigede_backend.controllers.capturers.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestGetCapturistsByNameDTO {
    private String name;
    @NotNull(message = "field.not.null")
    private int page;
    @NotNull(message = "field.not.null")
    private int size;
}
