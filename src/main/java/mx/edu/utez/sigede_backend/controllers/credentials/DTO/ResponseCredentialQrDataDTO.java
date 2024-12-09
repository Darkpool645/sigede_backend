package mx.edu.utez.sigede_backend.controllers.credentials.DTO;

import lombok.Data;
import mx.edu.utez.sigede_backend.controllers.credential_field.dto.ResponseCredentialFieldDTO;

import java.util.List;

@Data
public class ResponseCredentialQrDataDTO {
    private Long id;
    private String fullname;
    private String userPhoto;
    private String expirationDate;
    private String issueDate;
    private List<ResponseCredentialFieldDTO> fields;
}
