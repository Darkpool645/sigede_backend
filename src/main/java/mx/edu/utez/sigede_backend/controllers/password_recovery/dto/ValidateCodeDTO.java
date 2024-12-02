package mx.edu.utez.sigede_backend.controllers.password_recovery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ValidateCodeDTO {
    @NotNull(message = "code.required")
    private String code;
    @NotNull(message = "email.required")
    @Email(message = "email.invalid")
    private String userEmail;
}
