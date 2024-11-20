package mx.edu.utez.sigede_backend.controllers.credentials.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.controllers.credential_field.dto.RequestCredentialFieldDTO;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCredentialDTO {
    private Long userAccountId;
    private Long institutionId;
    private String fullname;
    private String userPhoto;
    private List<RequestCredentialFieldDTO> fields;
}
