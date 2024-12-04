package mx.edu.utez.sigede_backend.controllers.credentials.DTO;

import jakarta.validation.constraints.*;
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
    @NotBlank(message = "fullname.not.blank")
    @Size(min = 3, max = 50, message = "fullname.size")
    private String fullname;
    @NotBlank(message = "userphoto.not.blank")
    private String userPhoto;
    @Future(message = "expiration.date.future")
    private LocalDateTime expirationDate;
    @PastOrPresent(message = "issue.date.past.or.present")
    private LocalDateTime issueDate;
    @NotNull(message = "credential.fields.not.null")
    @Size(min = 1, message = "credential.fields.not.empty")
    private List<RequestUpdateCredentialFieldDTO> fields;
}
