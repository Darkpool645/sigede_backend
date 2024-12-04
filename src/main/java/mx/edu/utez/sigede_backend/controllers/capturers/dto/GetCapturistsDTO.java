package mx.edu.utez.sigede_backend.controllers.capturers.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetCapturistsDTO {
    private Long userAccountId;
    private String name;
    private String status;
}
