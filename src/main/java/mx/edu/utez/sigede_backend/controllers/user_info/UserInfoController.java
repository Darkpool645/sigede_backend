package mx.edu.utez.sigede_backend.controllers.user_info;

import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.InstitutionResponseDTO;
import mx.edu.utez.sigede_backend.controllers.institution_capturist_field.DTO.CapturistFieldResponseDTO;
import mx.edu.utez.sigede_backend.controllers.user_info.DTO.UserInfoDTO;
import mx.edu.utez.sigede_backend.models.institution_capturist_field.InstitutionCapturistField;
import mx.edu.utez.sigede_backend.services.user_info.UserInfoService;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user-info")
public class UserInfoController {

    private final UserInfoService userInfoService;
    private final ErrorDictionary errorDictionary;

    public UserInfoController(UserInfoService userInfoService, ErrorDictionary errorDictionary) {
        this.userInfoService = userInfoService;
        this.errorDictionary = errorDictionary;
    }

    @PostMapping("/create-form")
    public ResponseEntity<?> createFieldAndAssociate(
            @RequestBody UserInfoDTO userInfoDTO,
            @RequestParam Long institutionId,
            @RequestParam boolean isRequired) {
        try {
            InstitutionCapturistField result = userInfoService.createFieldAndAssociate(userInfoDTO, institutionId, isRequired);

            Map<String, Object> response = getStringObjectMap(result);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (CustomException e) {
            String errorMessage = errorDictionary.getErrorMessage(e.getErrorCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    private static Map<String, Object> getStringObjectMap(InstitutionCapturistField result) {
        InstitutionResponseDTO institutionDTO = new InstitutionResponseDTO(
                result.getFkInstitution().getInstitutionId(),
                result.getFkInstitution().getName(),
                result.getFkInstitution().getInstitutionStatus()
        );

        CapturistFieldResponseDTO fieldDTO = new CapturistFieldResponseDTO(
                result.isRequired(),
                result.getFkUserInfo().getTag(),
                result.getFkUserInfo().getType(),
                result.getFkUserInfo().isInQr(),
                result.getFkUserInfo().isInCard()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Campo creado exitosamente");
        response.put("institution", institutionDTO);
        response.put("field", fieldDTO);
        return response;
    }

    @PutMapping("/update-form/{fieldId}")
    public ResponseEntity<?> updateCapturistField(
            @PathVariable Long fieldId,
            @RequestParam boolean isRequired,
            @RequestBody UserInfoDTO userInfoDTO) {
        try {
            InstitutionCapturistField result = userInfoService.updateCapturistField(fieldId, isRequired, userInfoDTO);

            InstitutionResponseDTO responseDTO = new InstitutionResponseDTO(
                    result.getFkInstitution().getInstitutionId(),
                    result.getFkInstitution().getName(),
                    result.getFkInstitution().getInstitutionStatus()
            );

            Map<String, Object> updatedFields = new HashMap<>();
            updatedFields.put("isRequired", isRequired);
            updatedFields.put("tag", userInfoDTO.getTag());
            updatedFields.put("type", userInfoDTO.getType());
            updatedFields.put("inQr", userInfoDTO.isInQr());
            updatedFields.put("inCard", userInfoDTO.isInCard());

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Campos actualizados correctamente");
            response.put("institution", responseDTO);
            response.put("updatedFields", updatedFields);

            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            String errorMessage = errorDictionary.getErrorMessage(e.getErrorCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/get-institution-form/{institutionId}")
    public ResponseEntity<?> getFieldsByInstitution(@PathVariable Long institutionId) {
        try {
            Map<String, Object> result = userInfoService.getFieldsByInstitution(institutionId);
            return ResponseEntity.ok(result);
        } catch (CustomException e) {
            String errorMessage = errorDictionary.getErrorMessage(e.getErrorCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
