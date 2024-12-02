package mx.edu.utez.sigede_backend.controllers.password_recovery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ValidateCodeDTO {
    @NotBlank(message = "field.not.null")
    private String code;
    @NotNull(message = "field.not.null")
    private Long userId;
}
