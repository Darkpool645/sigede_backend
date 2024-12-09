package mx.edu.utez.sigede_backend.controllers.admin.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestUpdateBasicData {
    private Long userAccountId;
    private String name;
    private String email;
    private String status;
}
