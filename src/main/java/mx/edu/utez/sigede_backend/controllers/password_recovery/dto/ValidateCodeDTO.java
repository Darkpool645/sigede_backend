package mx.edu.utez.sigede_backend.controllers.password_recovery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static mx.edu.utez.sigede_backend.utils.validations.RegexPatterns.EMAIL_REGEX;

@Data
public class ValidateCodeDTO {
    @NotBlank(message = "field.not.null")
    private String code;
    @NotBlank(message = "field.not.null")
    @Pattern(regexp = EMAIL_REGEX, message = "user.email.invalid")
    private String userEmail;
}
