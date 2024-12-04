package mx.edu.utez.sigede_backend.controllers.doc_credential.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RequestDocCredentialDTO {
    @NotNull(message = "field.not.null")
    private Long institutionId;
    @NotNull(message = "field.not.null")
    private Long credentialId;
}
