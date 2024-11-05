package mx.edu.utez.sigede_backend.controllers.password_recovery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeResponseDTO {
    private String newPassword;
    private UUID userId;
}
