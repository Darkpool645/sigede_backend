package mx.edu.utez.sigede_backend.controllers.capturers;

import mx.edu.utez.sigede_backend.controllers.capturers.dto.RequestCapturerRegistrationDTO;
import mx.edu.utez.sigede_backend.controllers.capturers.dto.RequestUpdateBasicData;
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

    @GetMapping("/get-capturist/{userId}/{institutionId}")
    public CustomResponse<ResponseCapturistDTO> getCapturist(@PathVariable Long userId,@PathVariable Long institutionId) {
        if (userId == null) {
            throw new CustomException("Invalid user id");
        }
        if(institutionId == null) {
            throw new CustomException("Invalid institution id");
        }
        ResponseCapturistDTO response = service.getOneCapturer(userId,institutionId);
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

    @PutMapping("/update-basic-data")
    public  CustomResponse<Long> updateBasicData(@Validated @RequestBody RequestUpdateBasicData payload) {
        boolean result = service.updateBasicData(payload);

        if (result) {
            return new CustomResponse<>(200, "Informacion actualizada correctamente", false, payload.getUserAccountId());
        } else {
            return new CustomResponse<>(500, "Ocurrio un error inesperado.", false, null);
        }
    }
}
