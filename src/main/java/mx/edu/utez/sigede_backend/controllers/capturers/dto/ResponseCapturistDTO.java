package mx.edu.utez.sigede_backend.controllers.capturers.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ResponseCapturistDTO {
    private Long userAccountId;
    private String name;
    private String email;
    private String status;
}
