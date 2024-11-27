package mx.edu.utez.sigede_backend.controllers.credential_field.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateCredentialFieldDTO {
    private Long credentialFieldId;
    private String value;
    private String tag;
}
