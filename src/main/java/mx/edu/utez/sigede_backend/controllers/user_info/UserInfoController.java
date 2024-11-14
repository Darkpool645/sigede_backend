package mx.edu.utez.sigede_backend.controllers.user_info;

import mx.edu.utez.sigede_backend.controllers.user_info.dto.RequestNewUserInfoDTO;
import mx.edu.utez.sigede_backend.services.user_info.UserInfoService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user-info")
public class UserInfoController {
    private final UserInfoService service;

    public UserInfoController(UserInfoService service) {
        this.service = service;
    }

    @PostMapping("/save-data")
    public CustomResponse<String> postUserInfo(@Validated @RequestBody RequestNewUserInfoDTO payload){
        service.saveUserInfo(payload);
        return new CustomResponse<>(201,"Información registrada correctamente",false,null);
    }
}
