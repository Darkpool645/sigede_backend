package mx.edu.utez.sigede_backend.controllers.user_info.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoUpdateDTO {
    private Long fieldId;
    @Getter
    private Boolean isRequired;
    private String tag;
    private String type;
    private boolean isInQr;
    private boolean isInCard;

}
