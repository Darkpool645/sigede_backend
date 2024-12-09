package mx.edu.utez.sigede_backend.controllers.password_recovery.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import static mx.edu.utez.sigede_backend.utils.validations.RegexPatterns.EMAIL_REGEX;

@Data
public class UserEmailDTO {
    @NotBlank(message = "user.email.notnull")
    @Pattern(regexp = EMAIL_REGEX, message = "user.email.invalid")
    private String userEmail;
}
