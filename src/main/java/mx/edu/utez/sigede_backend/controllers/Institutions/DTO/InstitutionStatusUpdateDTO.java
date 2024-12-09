package mx.edu.utez.sigede_backend.controllers.Institutions.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstitutionStatusUpdateDTO {
    private Long institutionId;
    private String institutionStatus;
}