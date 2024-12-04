package mx.edu.utez.sigede_backend.controllers.user_accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUserBasicInfoDTO {
    private Long userAccountId;
    private String name;
    private String status;
}
