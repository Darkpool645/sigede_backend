package mx.edu.utez.sigede_backend.controllers.credential_field.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.credential_field.CredentialField;

@Data
@NoArgsConstructor
public class ResponseCredentialFieldDTO {
    private String value;
    private String tag;
    private String type;
    private boolean is_in_card;
    private boolean is_in_qr;

    public ResponseCredentialFieldDTO(CredentialField field) {
        this.value = field.getValue();
        this.tag = field.getFkUserInfo().getTag();
        this.type = field.getFkUserInfo().getType();
        this.is_in_card = field.getFkUserInfo().isInCard();
        this.is_in_qr = field.getFkUserInfo().isInQr();
    }
}
