package mx.edu.utez.sigede_backend.controllers.user_accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestRegisterSuperAdminDTO {
    @NotBlank(message = "user.email.notnull")
    @Email(message = "user.email.invalid")
    private String email;

    @NotBlank(message = "user.password.notnull")
    private String password;

    @NotBlank(message = "user.name.notnull")
    private String name;
}
