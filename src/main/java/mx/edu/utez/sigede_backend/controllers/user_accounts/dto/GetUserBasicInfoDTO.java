package mx.edu.utez.sigede_backend.controllers.user_accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetUserBasicInfoDTO {
    private Long userAccountId;
    private String name;
    private String status;
}
