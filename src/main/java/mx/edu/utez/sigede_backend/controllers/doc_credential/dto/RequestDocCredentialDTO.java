package mx.edu.utez.sigede_backend.controllers.doc_credential.dto;

import lombok.Data;

@Data
public class RequestDocCredentialDTO {
    private Long institutionId;
    private Long credentialId;
}
