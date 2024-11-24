package mx.edu.utez.sigede_backend.controllers.user_accounts.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseAllAdminByInstitutionDTO {
    private Long userId;
    private String email;
    private String name;


}
