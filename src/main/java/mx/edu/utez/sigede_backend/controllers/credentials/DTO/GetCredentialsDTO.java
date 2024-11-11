package mx.edu.utez.sigede_backend.controllers.credentials.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class GetCredentialsDTO {
    private Long credentialId;
    private String fullname;
    private byte[] userPhoto;
}