package mx.edu.utez.sigede_backend.controllers.capturers;

import mx.edu.utez.sigede_backend.controllers.capturers.dto.RequestCapturerRegistrationDTO;
import mx.edu.utez.sigede_backend.services.capturer.CapturerService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/capturers")
public class CapturerController {
    private final CapturerService service;

    public CapturerController(CapturerService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<Object> registerCapturer(@Validated @RequestBody RequestCapturerRegistrationDTO payload) {
        service.registerCapturer(payload);
        CustomResponse<String> response = new CustomResponse<>(201,"Cuenta registrada correctamente", false,null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
