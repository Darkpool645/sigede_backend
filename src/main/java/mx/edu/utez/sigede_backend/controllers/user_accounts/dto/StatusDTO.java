package mx.edu.utez.sigede_backend.controllers.user_accounts.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StatusDTO {
    private String name;

    public StatusDTO(String name) {
        this.name = name;
    }
}
