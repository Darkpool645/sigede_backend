package mx.edu.utez.sigede_backend.controllers.capturers;

import mx.edu.utez.sigede_backend.controllers.capturers.dto.RequestCapturerRegistrationDTO;
import mx.edu.utez.sigede_backend.controllers.capturers.dto.ResponseCapturistDTO;
import mx.edu.utez.sigede_backend.services.capturer.CapturerService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/capturists")
public class CapturerController {
    private final CapturerService service;

    public CapturerController(CapturerService service) {
        this.service = service;
    }

    @GetMapping("/get-capturist/{userId}")
    public CustomResponse<ResponseCapturistDTO> getCapturist(@PathVariable Long userId) {
        if (userId == null) {
            throw new CustomException("Invalid user id");
        }
        ResponseCapturistDTO response = service.getOneCapturer(userId);
        return new CustomResponse<>(200, "Capturista encontrado correctamente.", false, response);
    }

    @PostMapping("/register")
    public CustomResponse<Object> registerCapturer(@Validated @RequestBody RequestCapturerRegistrationDTO payload) {
        service.registerCapturer(payload);
        return new CustomResponse<>(201,"Cuenta registrada correctamente", false,null);
    }

    @PatchMapping("/change-status")
    public CustomResponse<Long> changeCapturistStatus(Long userId) {
        if (userId == null) {
            throw new CustomException("Invalid user id");
        }
        boolean result = service.changeCapturistStatus(userId);
        if (result) {
            return new CustomResponse<>(200, "Estatus actualizado correctamente", false, userId);
        } else {
            return new CustomResponse<>(500, "Ocurrio un error inesperado.", false, null);
        }
    }
}
