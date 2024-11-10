package mx.edu.utez.sigede_backend.controllers.capturers;

import mx.edu.utez.sigede_backend.controllers.capturers.dto.RequestCapturerRegistrationDTO;
import mx.edu.utez.sigede_backend.services.capturer.CapturerService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/capturers")
public class CapturerController {
    private final CapturerService service;

    public CapturerController(CapturerService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public CustomResponse<Object> registerCapturer(@Validated @RequestBody RequestCapturerRegistrationDTO payload) {
        service.registerCapturer(payload);
        return new CustomResponse<>(201,"Cuenta registrada correctamente", false,null);
    }
}
