package mx.edu.utez.sigede_backend.controllers.user_accounts.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestEditStatusDTO {
    @NotNull(message = "user.id.required")
    private Long userId;
    @NotNull(message = "status.required")
    private Long status;
}
