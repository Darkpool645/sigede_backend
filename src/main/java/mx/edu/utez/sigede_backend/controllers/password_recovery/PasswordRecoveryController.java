package mx.edu.utez.sigede_backend.controllers.password_recovery;

import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.PasswordChangeRequestDTO;
import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.PasswordChangeResponseDTO;
import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.UserEmailDTO;
import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.ValidateCodeDTO;
import mx.edu.utez.sigede_backend.services.password_recovery.PasswordRecoveryService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/recovery-password")
@CrossOrigin(origins = {"*"})
public class PasswordRecoveryController {
    private final PasswordRecoveryService passwordRecoveryService;

    public PasswordRecoveryController(PasswordRecoveryService passwordRecoveryService) {
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @PostMapping("/send-verification-code")
    public CustomResponse<Long> sendVerificationCode(@RequestBody UserEmailDTO userEmailDTO) {
        try {
            Long userId = passwordRecoveryService.sendVerificationCode(userEmailDTO.getUserEmail());
            return new CustomResponse<>(
                    200, "Código de verificación enviado correctamente.", false, userId
            );
        } catch (CustomException e) {
            return new CustomResponse<>(
                    404, "El usuario que ha ingresado no existe.", true, null);
        }
    }

    @PostMapping("/resend-verification-code")
    public CustomResponse<Long> resendVerificationCode(@RequestBody UserEmailDTO userEmailDTO) {
        try {
            Long userId = passwordRecoveryService.resendVerificationCode(userEmailDTO.getUserEmail());
            return new CustomResponse<>(
                    200, "Código de verificación enviado correctamente.", false, userId);
        } catch (CustomException e) {
            return new CustomResponse<>(
                    400, "Ocurrio un error al enviar el código de verificación.", true, null
            );
        }
    }

    @PostMapping("/validate-verification-code")
    public CustomResponse<Long> validateVerificationCode(@RequestBody ValidateCodeDTO validateCodeDTO) {
        try {
            boolean result = passwordRecoveryService.validateVerificationCode(validateCodeDTO.getCode(), validateCodeDTO.getUserId());
            if (result) {
                passwordRecoveryService.deleteVerificationCode(validateCodeDTO.getUserId());
                return new CustomResponse<>(
                        200, "El código es valido.", false, validateCodeDTO.getUserId()
                );
            } else {
                return new CustomResponse<>(400, "El código ingresado no es valido.", true, null);
            }
        } catch (CustomException e) {
            return new CustomResponse<>(
                    400, e.getMessage(), true, null);
        }
    }

    @PutMapping("/change-password")
    public CustomResponse<PasswordChangeResponseDTO> changePassword(@RequestBody PasswordChangeRequestDTO passwordChangeRequestDTO) {
        try {
            PasswordChangeResponseDTO responseDTO = passwordRecoveryService.changePassword(
                    passwordChangeRequestDTO.getNewPassword(), passwordChangeRequestDTO.getUserId());
            return new CustomResponse<>(
                    200, "La contraseña ha sido cambiada correctamente", false,
                    responseDTO
            );
        } catch (CustomException e) {
            return new CustomResponse<>(
                    400, e.getMessage(), true, null
            );
        }
    }
}
