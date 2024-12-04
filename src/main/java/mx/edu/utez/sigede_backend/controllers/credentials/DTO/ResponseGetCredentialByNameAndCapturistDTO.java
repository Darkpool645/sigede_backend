package mx.edu.utez.sigede_backend.controllers.credentials.DTO;

import lombok.Data;

@Data
public class ResponseGetCredentialByNameAndCapturistDTO {
    private String credentialName;
    private String photo;
}
