package mx.edu.utez.sigede_backend.controllers.credentials.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCretentialsByInstitutoIdDTO {
    private Long credentialId;
    private String fullname;
    private String userPhoto;
    private LocalDateTime expirationDate;
}
