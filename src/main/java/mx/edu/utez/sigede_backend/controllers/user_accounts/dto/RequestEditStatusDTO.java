package mx.edu.utez.sigede_backend.controllers.user_accounts.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestEditStatusDTO {
    @NotNull(message = "user.email.notnull")
    @Email(message = "user.email.invalid")
    private String email;
    @NotNull(message = "status.required")
    private String status;
}
