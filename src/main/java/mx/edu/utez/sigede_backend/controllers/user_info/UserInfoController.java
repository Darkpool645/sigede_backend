package mx.edu.utez.sigede_backend.controllers.user_info;

import mx.edu.utez.sigede_backend.controllers.institution_capturist_field.DTO.InstitutionCapturistFieldDTO;
import mx.edu.utez.sigede_backend.controllers.user_info.DTO.UserInfoDTO;
import mx.edu.utez.sigede_backend.models.institution_capturist_field.InstitutionCapturistField;
import mx.edu.utez.sigede_backend.models.user_info.UserInfo;
import mx.edu.utez.sigede_backend.services.user_info.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/user-info-by-institution/{institutionId}")
    public List<UserInfo> getUserInfoByInstitution(@PathVariable Long institutionId) {
        return userInfoService.getUserInfoByInstitution(institutionId);
    }

    @PostMapping("/user-info/post")
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfo createUserInfo(@RequestBody UserInfoDTO userInfoDTO) {
        return userInfoService.createUserInfo(userInfoDTO);
    }

    @GetMapping("/capturist-fields/institution/{institutionId}")
    public List<InstitutionCapturistField> getCapturistFieldsByInstitution(@PathVariable Long institutionId) {
        return userInfoService.getCapturistFieldsByInstitution(institutionId);
    }

    @PostMapping("/user-info/post")
    @ResponseStatus(HttpStatus.CREATED)
    public InstitutionCapturistField createCapturistField(@RequestBody InstitutionCapturistFieldDTO capturistFieldDTO) {
        return userInfoService.createCapturistField(capturistFieldDTO);
    }

    @PutMapping("/user-info/{fieldId}")
    public void updateCapturistField(@PathVariable Long fieldId, @RequestBody InstitutionCapturistFieldDTO capturistFieldDTO) {
        userInfoService.updateCapturistField(fieldId, capturistFieldDTO);
    }
}
