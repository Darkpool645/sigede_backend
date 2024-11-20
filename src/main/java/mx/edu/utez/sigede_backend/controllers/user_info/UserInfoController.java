package mx.edu.utez.sigede_backend.controllers.user_info;

import mx.edu.utez.sigede_backend.controllers.user_info.DTO.UserInfoDTO;
import mx.edu.utez.sigede_backend.models.institution_capturist_field.InstitutionCapturistField;
import mx.edu.utez.sigede_backend.services.user_info.UserInfoService;
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

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @PostMapping("/create-form")
    public ResponseEntity<?> createFieldAndAssociate(
            @RequestBody UserInfoDTO userInfoDTO,
            @RequestParam Long institutionId,
            @RequestParam boolean isRequired) {
        try {
            InstitutionCapturistField result = userInfoService.createFieldAndAssociate(userInfoDTO, institutionId, isRequired);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/update-form/{fieldId}")
    public ResponseEntity<?> updateCapturistField(
            @PathVariable Long fieldId,
            @RequestParam boolean isRequired,
            @RequestBody UserInfoDTO userInfoDTO) {
        try {
            InstitutionCapturistField result = userInfoService.updateCapturistField(fieldId, isRequired, userInfoDTO);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/get-institution-form/{institutionId}")
    public ResponseEntity<?> getFieldsByInstitution(@PathVariable Long institutionId) {
        try {
            List<InstitutionCapturistField> result = userInfoService.getFieldsByInstitution(institutionId);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
