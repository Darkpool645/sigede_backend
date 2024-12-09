package mx.edu.utez.sigede_backend.controllers.password_recovery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static mx.edu.utez.sigede_backend.utils.validations.RegexPatterns.EMAIL_REGEX;
import static mx.edu.utez.sigede_backend.utils.validations.RegexPatterns.PASSWORD_REGEX;


@Data
public class PasswordChangeRequestDTO {
    @NotBlank(message = "field.not.null")
    private String newPassword;
    @NotBlank(message = "field.not.null")
    @Pattern(regexp = EMAIL_REGEX, message = "user.email.invalid")
    private String userEmail;
}
