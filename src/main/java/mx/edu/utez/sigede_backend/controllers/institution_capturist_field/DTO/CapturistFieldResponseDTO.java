package mx.edu.utez.sigede_backend.controllers.institution_capturist_field.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CapturistFieldResponseDTO {
    private boolean isRequired;
    private String tag;
    private String type;
    private boolean inQr;
    private boolean inCard;
}
