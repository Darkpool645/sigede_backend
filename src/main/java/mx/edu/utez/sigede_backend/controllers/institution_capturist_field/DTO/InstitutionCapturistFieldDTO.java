package mx.edu.utez.sigede_backend.controllers.institution_capturist_field.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionCapturistFieldDTO {
    private Long institutionCapturistFieldId;
    private boolean isRequired;
    private Long institutionId;
    private Long userInfoId;
}
