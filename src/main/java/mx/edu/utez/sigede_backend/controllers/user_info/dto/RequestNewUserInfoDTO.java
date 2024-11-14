package mx.edu.utez.sigede_backend.controllers.user_info.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestNewUserInfoDTO {
    private boolean is_in_card;
    private boolean is_in_qr;
    private String tag;
    private String type;
}
