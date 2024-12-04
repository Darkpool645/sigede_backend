package mx.edu.utez.sigede_backend.controllers.credential_field.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCredentialFieldDTO {
    private String tag;
    private String value;
}
