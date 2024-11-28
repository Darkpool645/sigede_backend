package mx.edu.utez.sigede_backend.controllers.user_info;

import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.InstitutionResponseDTO;
import mx.edu.utez.sigede_backend.controllers.institution_capturist_field.DTO.CapturistFieldResponseDTO;
import mx.edu.utez.sigede_backend.controllers.user_info.DTO.UserInfoPostDTO;
import mx.edu.utez.sigede_backend.controllers.user_info.DTO.UserInfoUpdateDTO;
import mx.edu.utez.sigede_backend.models.institution_capturist_field.InstitutionCapturistField;
import mx.edu.utez.sigede_backend.services.user_info.UserInfoService;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
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

    @PostMapping("/create-forms")
    public ResponseEntity<?> createFieldAndAssociate(
            @RequestBody List<UserInfoPostDTO> userInfoDTOList,
            @RequestParam Long institutionId) {

        try {
            List<InstitutionCapturistField> result = userInfoService.createFieldAndAssociate(userInfoDTOList, institutionId);

            List<Map<String, Object>> response = result.stream().map(field -> {
                InstitutionResponseDTO institutionDTO = new InstitutionResponseDTO(
                        field.getFkInstitution().getInstitutionId(),
                        field.getFkInstitution().getName(),
                        field.getFkInstitution().getInstitutionStatus()
                );

                CapturistFieldResponseDTO fieldDTO = new CapturistFieldResponseDTO(
                        field.isRequired(),
                        field.getFkUserInfo().getTag(),
                        field.getFkUserInfo().getType(),
                        field.getFkUserInfo().isInQr(),
                        field.getFkUserInfo().isInCard()
                );

                Map<String, Object> map = new HashMap<>();
                map.put("institution", institutionDTO);
                map.put("field", fieldDTO);
                return map;
            }).toList();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (CustomException e) {
            String errorMessage = errorDictionary.getErrorMessage(e.getErrorCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }


    @PutMapping("/update-forms")
    public ResponseEntity<?> updateCapturistFields(@RequestBody List<UserInfoUpdateDTO> updates) {
        for (UserInfoUpdateDTO update : updates) {
        }
        try {
            List<Map<String, Object>> responseList = userInfoService.updateCapturistFields(updates);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Campos actualizados correctamente");
            response.put("updatedFields", responseList);

            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            String errorMessage = errorDictionary.getErrorMessage(e.getErrorCode());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", errorMessage);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
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
