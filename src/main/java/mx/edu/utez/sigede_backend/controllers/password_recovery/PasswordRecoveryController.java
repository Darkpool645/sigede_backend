package mx.edu.utez.sigede_backend.controllers.password_recovery;

import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.PasswordChangeRequestDTO;
import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.PasswordChangeResponseDTO;
import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.UserEmailDTO;
import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.ValidateCodeDTO;
import mx.edu.utez.sigede_backend.services.password_recovery.PasswordRecoveryService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/credentials/api/recovery-password")
@CrossOrigin(origins = {"*"})
public class PasswordRecoveryController {
    private final PasswordRecoveryService passwordRecoveryService;

    public PasswordRecoveryController(PasswordRecoveryService passwordRecoveryService) {
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @PostMapping("/send-verification-code")
    ResponseEntity<Object> sendVerificationCode(@RequestBody UserEmailDTO userEmailDTO) {
        try {
            UUID userId = passwordRecoveryService.sendVerificationCode(userEmailDTO.getUserEmail());
            return new ResponseEntity<>(new CustomResponse<>(
                    200, "Código de verificación enviado correctamente.", false, userId), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(new CustomResponse<>(
                    400, "El usuario que ha ingresado no existe.", true, null), HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("/resend-verification-code")
    ResponseEntity<Object> resendVerificationCode(@RequestBody UserEmailDTO userEmailDTO) {
        try {
            UUID userId = passwordRecoveryService.resendVerificationCode(userEmailDTO.getUserEmail());
            return new ResponseEntity<>(new CustomResponse<>(
                    200, "Código de verificación enviado correctamente.", false, userId), HttpStatus.OK);
        } catch (CustomException e) {
            return new ResponseEntity<>(new CustomResponse<>(
               400, "Ocurrio un error al enviar el código de verificación.", true, null
            ), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/validate-verification-code")
    ResponseEntity<Object> validateVerificationCode(@RequestBody ValidateCodeDTO validateCodeDTO) {
        try {
            boolean result = passwordRecoveryService.validateVerificationCode(validateCodeDTO.getCode(), validateCodeDTO.getUserId());
            if (result) {
                passwordRecoveryService.deleteVerificationCode(validateCodeDTO.getUserId());
                return new ResponseEntity<>(new CustomResponse<>(
                        200, "El código es valido.", false, null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new CustomResponse<>(
                        400, "El código ingresado no es valido.", true, null
                ), HttpStatus.BAD_REQUEST);
            }
        } catch (CustomException e) {
            return  new ResponseEntity<>(new CustomResponse<>(
                    400, e.getMessage(), true, null), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/change-password")
    ResponseEntity<Object> changePassword(@RequestBody PasswordChangeRequestDTO passwordChangeRequestDTO) {
        try {
            PasswordChangeResponseDTO responseDTO = passwordRecoveryService.changePassword(
                    passwordChangeRequestDTO.getNewPassword(), passwordChangeRequestDTO.getUserId());
            return new ResponseEntity<>(new CustomResponse<>(
                    200, "La contraseña ha sido cambiada correctamente", false,
                    responseDTO), HttpStatus.OK);
            //servicio para enviar email
        } catch (CustomException e) {
            return new ResponseEntity<>(new CustomResponse<>(400, e.getMessage(), true, null), HttpStatus.BAD_REQUEST);
        }
    }
}
