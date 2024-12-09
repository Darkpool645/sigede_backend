package mx.edu.utez.sigede_backend.controllers.capturers;

import mx.edu.utez.sigede_backend.controllers.admin.dto.RequestUpdateBasicData;
import mx.edu.utez.sigede_backend.controllers.capturers.dto.RequestCapturerRegistrationDTO;
import mx.edu.utez.sigede_backend.controllers.capturers.dto.RequestGetCapturistsByNameAndInstitutionDTO;
import mx.edu.utez.sigede_backend.controllers.capturers.dto.RequestGetCapturistsByNameDTO;
import mx.edu.utez.sigede_backend.controllers.capturers.dto.ResponseCapturistDTO;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.services.capturer.CapturerService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/capturists")
public class CapturerController {
    private final CapturerService capturerService;

    public CapturerController(CapturerService capturerService) {
        this.capturerService = capturerService;
    }

    @GetMapping("/get-capturist/{userId}/{institutionId}")
    public CustomResponse<ResponseCapturistDTO> getCapturist(@PathVariable Long userId, @PathVariable Long institutionId) {
        if (userId == null) {
            throw new CustomException("user.id.required");
        }
      
        if (institutionId == null) {

            throw new CustomException("institution.id.notnull");
        }
        ResponseCapturistDTO response = capturerService.getOneCapturer(userId, institutionId);
        return new CustomResponse<>(200, "Capturista encontrado correctamente.", false, response);
    }

    @GetMapping("/get-capturistId/{email}")
    public CustomResponse<Long> getCapturist(@PathVariable String email) {
        if (email == null) {

            throw new CustomException("user.email.invalid");
        }
        Long id = capturerService.getCapturistIdByEmail(email);
        if (id == null) {
            return new CustomResponse<>(400, "Capturista no encontrado .", true, null);
        }

        return new CustomResponse<>(200, "Capturista encontrado correctamente.", false, id);
    }

    @PostMapping("/get-capturists-by-name")
    public CustomResponse<Page<ResponseCapturistDTO>> getCapturistsByName(@Validated @RequestBody RequestGetCapturistsByNameDTO request) {
        Page<UserAccount> pages = capturerService.getCapturistByName(request.getName(), request.getPage(), request.getSize());

        Page<ResponseCapturistDTO> response = castToResponseDTO(pages);

        return new CustomResponse<>(200, "Capturistas filtrados correctamente.", false, response);
    }

    @PostMapping("/get-capturists-by-name-and-institution")
    public CustomResponse<Page<ResponseCapturistDTO>> getCapturistsByNameAndInstitution(@Validated @RequestBody RequestGetCapturistsByNameAndInstitutionDTO dto) {
        Page<UserAccount> pages = capturerService.getCapturistsByNameAndInstitution(dto.getName(), dto.getInstitutionId(),
                dto.getPage(), dto.getSize());

        Page<ResponseCapturistDTO> response = castToResponseDTO(pages);

        return new CustomResponse<>(200, "Capturistas filtrados correctamente.", false, response);
    }

    @PostMapping("/register")
    public CustomResponse<Object> registerCapturer(@Validated @RequestBody RequestCapturerRegistrationDTO payload) {

        capturerService.registerCapturer(payload);
        return new CustomResponse<>(201,"Cuenta registrada correctamente", false,null);
    }

    @PatchMapping("/change-status")
    public CustomResponse<Long> changeCapturistStatus(Long userId) {
        if (userId == null) {
            throw new CustomException("user.id.required");
        }
        boolean result = capturerService.changeCapturistStatus(userId);
        if (result) {
            return new CustomResponse<>(200, "Estatus actualizado correctamente", false, userId);
        } else {
            return new CustomResponse<>(500, "Ocurrio un error inesperado.", false, null);
        }
    }


    @PutMapping("/update-basic-data")
    public CustomResponse<Long> updateBasicData(@Validated @RequestBody RequestUpdateBasicData payload) {
        boolean result = capturerService.updateBasicData(payload); 
        if (result) {
            return new CustomResponse<>(200, "Informacion actualizada correctamente", false, payload.getUserAccountId());
        } else {
            return new CustomResponse<>(500, "Ocurrio un error inesperado.", false, null);
        }

    }

    private Page<ResponseCapturistDTO> castToResponseDTO(Page<UserAccount> pages) {
        return pages.map(userAccount -> {
            ResponseCapturistDTO dtoResponse = new ResponseCapturistDTO();
            dtoResponse.setUserAccountId(userAccount.getUserAccountId());
            dtoResponse.setName(userAccount.getName());
            dtoResponse.setEmail(userAccount.getEmail());
            dtoResponse.setStatus(userAccount.getFkStatus().getName());
            return dtoResponse;
        });
    }
}
