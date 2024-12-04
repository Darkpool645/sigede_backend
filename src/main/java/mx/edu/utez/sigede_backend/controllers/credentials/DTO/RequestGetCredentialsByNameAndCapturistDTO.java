package mx.edu.utez.sigede_backend.controllers.credentials.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestGetCredentialsByNameAndCapturistDTO {
    @NotBlank(message = "field.not.null")
    private String name;
    @NotNull(message = "field.not.null")
    private Long capturistId;
    @NotNull(message = "field.not.null")
    private int page;
    @NotNull(message = "field.not.null")
    private int size;
}
