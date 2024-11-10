package mx.edu.utez.sigede_backend.controllers.user_accounts.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class RoleDTO {
    private String name;

    public RoleDTO(String name) {
        this.name = name;
    }
}
