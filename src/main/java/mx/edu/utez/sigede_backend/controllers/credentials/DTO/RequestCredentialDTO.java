package mx.edu.utez.sigede_backend.controllers.credentials.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.controllers.credential_field.dto.RequestCredentialFieldDTO;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCredentialDTO {
    @NotNull(message = "user.id.required")
    private Long userAccountId;
    @NotNull(message = "institution.id.notnull")
    private Long institutionId;
    @NotBlank(message = "fullname.not.blank")
    @Size(message = "fullname.size", max = 50)
    private String fullname;
    @NotBlank(message = "userphoto.not.blank")
    private String userPhoto;
    @NotNull(message = "credential.fields.not.null")
    @Size(min = 1, message = "credential.fields.not.empty")
    private List<RequestCredentialFieldDTO> fields;
}
