package mx.edu.utez.sigede_backend.controllers.password_recovery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static mx.edu.utez.sigede_backend.utils.validations.RegexPatterns.EMAIL_REGEX;
import static mx.edu.utez.sigede_backend.utils.validations.RegexPatterns.PASSWORD_REGEX;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PasswordChangeRequestDTO {
    @NotNull(message = "password.required")
    private String newPassword;
    @NotNull(message = "email.required")
    @Email(message = "email.invalid")
    @NotBlank(message = "field.not.null")
    @Pattern(regexp = EMAIL_REGEX, message = "user.email.invalid")
    private String userEmail;
}
