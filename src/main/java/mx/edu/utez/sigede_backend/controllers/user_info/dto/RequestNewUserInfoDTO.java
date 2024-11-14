package mx.edu.utez.sigede_backend.controllers.user_info.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class RequestNewUserInfoDTO {
    @NotNull(message = "isincard.notnull")
    private boolean is_in_card;
    @NotNull(message = "isinqr.notnull")
    private boolean is_in_qr;
    @NotBlank(message = "tag.notnull")
    @Size(max = 50, message = "tag.size")
    private String tag;
    @NotNull(message = "type.notnull")
    private String type;
}
