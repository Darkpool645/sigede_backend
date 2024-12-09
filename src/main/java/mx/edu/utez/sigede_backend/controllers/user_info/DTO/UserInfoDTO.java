package mx.edu.utez.sigede_backend.controllers.user_info.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoDTO {
    private Long userInfoId;
    private String tag;
    private String type;
    private boolean isInQr;
    private boolean isInCard;
}
