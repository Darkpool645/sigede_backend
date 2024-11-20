package mx.edu.utez.sigede_backend.controllers.credentials.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.controllers.credential_field.dto.RequestUpdateCredentialFieldDTO;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUpdateCredentialDTO {
    private Long id;
    private String fullname;
    private String userPhoto;
    private LocalDateTime expirationDate;
    private LocalDateTime issueDate;
    private List<RequestUpdateCredentialFieldDTO> fields;
}
