package mx.edu.utez.sigede_backend.services.user_info;

import jakarta.transaction.Transactional;
import mx.edu.utez.sigede_backend.controllers.user_info.dto.RequestNewUserInfoDTO;
import mx.edu.utez.sigede_backend.models.user_info.UserInfo;
import mx.edu.utez.sigede_backend.models.user_info.UserInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {
    private final UserInfoRepository userInfoRepository;

    public UserInfoService(UserInfoRepository userInfoRepository) {
        this.userInfoRepository = userInfoRepository;
    }

    @Transactional
    public void saveUserInfo (RequestNewUserInfoDTO payload){
        UserInfo userInfo = new UserInfo();
        userInfo.setInCard(payload.is_in_card());
        userInfo.setInCard(payload.is_in_qr());
        userInfo.setTag(payload.getTag());
        userInfo.setType(payload.getType());
        userInfoRepository.save(userInfo);
    }
}
