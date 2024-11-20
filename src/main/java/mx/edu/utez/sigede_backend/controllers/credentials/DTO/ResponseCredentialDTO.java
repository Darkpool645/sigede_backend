package mx.edu.utez.sigede_backend.controllers.credentials.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.controllers.credential_field.dto.ResponseCredentialFieldDTO;
import mx.edu.utez.sigede_backend.models.credential.Credential;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCredentialDTO {
    private Long id;
    private String fullname;
    private String userPhoto;
    private LocalDateTime expirationDate;
    private LocalDateTime issueDate;
    private List<ResponseCredentialFieldDTO> fields;

}
