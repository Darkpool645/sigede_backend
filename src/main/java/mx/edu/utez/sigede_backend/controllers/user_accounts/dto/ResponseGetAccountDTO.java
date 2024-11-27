package mx.edu.utez.sigede_backend.controllers.user_accounts.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseGetAccountDTO {
    private String email;
    private String name;
    private String rol;
    private String status;
    private Long institution;

    public ResponseGetAccountDTO(String email, String name, String role, String status,Long institution) {
        this.email=email;
        this.name = name;
        this.rol = role;
        this.status = status;
        this.institution = institution;
    }
}
