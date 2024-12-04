package mx.edu.utez.sigede_backend.controllers.user_accounts.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestEditDataDTO {
    @NotNull(message = "user.id.required")
    private Long userId;
    private String name;
    private String password;
    private Long fkRol;
    private Long fkStatus;
}
