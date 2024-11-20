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


    public ResponseCredentialFieldDTO(CredentialField field) {
        this.value = field.getValue();
        this.tag = field.getFkUserInfo().getTag();
    }
}
