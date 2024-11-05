package mx.edu.utez.sigede_backend.controllers.mailcontroller.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserIdAndLogoDTO {
    private UUID userId;
    private String logo;
}
