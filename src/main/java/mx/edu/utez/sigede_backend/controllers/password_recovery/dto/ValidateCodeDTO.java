package mx.edu.utez.sigede_backend.controllers.password_recovery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static mx.edu.utez.sigede_backend.utils.validations.RegexPatterns.EMAIL_REGEX;

@Data
public class ValidateCodeDTO {
    @NotNull(message = "code.required")
    private String code;
    @NotNull(message = "email.required")
    @Email(message = "email.invalid")
    @NotBlank(message = "field.not.null")
    @Pattern(regexp = EMAIL_REGEX, message = "user.email.invalid")
    private String userEmail;
}
