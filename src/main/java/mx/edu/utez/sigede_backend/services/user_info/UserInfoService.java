package mx.edu.utez.sigede_backend.services.user_info;

import mx.edu.utez.sigede_backend.controllers.institution_capturist_field.DTO.InstitutionCapturistFieldDTO;
import mx.edu.utez.sigede_backend.controllers.user_info.DTO.UserInfoDTO;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;
import mx.edu.utez.sigede_backend.models.institution_capturist_field.InstitutionCapturistField;
import mx.edu.utez.sigede_backend.models.institution_capturist_field.InstitutionCapturistFieldRepository;
import mx.edu.utez.sigede_backend.models.user_info.UserInfo;
import mx.edu.utez.sigede_backend.models.user_info.UserInfoRepository;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private InstitutionCapturistFieldRepository institutionCapturistFieldRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @Autowired
    private ErrorDictionary errorDictionary;

    public List<UserInfo> getUserInfoByInstitution(Long institutionId) {
        return userInfoRepository.findAll();
    }

    public UserInfo createUserInfo(UserInfoDTO userInfoDTO) {
        UserInfo userInfo = new UserInfo();
        userInfo.setTag(userInfoDTO.getTag());
        userInfo.setType(userInfoDTO.getType());
        userInfo.setInQr(userInfoDTO.isInQr());
        userInfo.setInCard(userInfoDTO.isInCard());

        return userInfoRepository.save(userInfo);
    }

    public InstitutionCapturistField createCapturistField(InstitutionCapturistFieldDTO capturistFieldDTO) {
        InstitutionCapturistField capturistField = new InstitutionCapturistField();
        capturistField.setRequired(capturistFieldDTO.isRequired());
        capturistField.setFkInstitution(institutionRepository.getOne(capturistFieldDTO.getInstitutionId()));
        capturistField.setFkUserInfo(userInfoRepository.getOne(capturistFieldDTO.getUserInfoId()));

        return institutionCapturistFieldRepository.save(capturistField);
    }

    public List<InstitutionCapturistField> getCapturistFieldsByInstitution(Long institutionId) {
        return institutionCapturistFieldRepository.findAll();
    }

    public void updateCapturistField(Long fieldId, InstitutionCapturistFieldDTO capturistFieldDTO) {
        Optional<InstitutionCapturistField> existingField = institutionCapturistFieldRepository.findById(fieldId);
        if (existingField.isPresent()) {
            InstitutionCapturistField field = existingField.get();
            field.setRequired(capturistFieldDTO.isRequired());
            field.setFkInstitution(institutionRepository.getOne(capturistFieldDTO.getInstitutionId()));
            field.setFkUserInfo(userInfoRepository.getOne(capturistFieldDTO.getUserInfoId()));
            institutionCapturistFieldRepository.save(field);
        } else {
            throw new RuntimeException(errorDictionary.getErrorMessage("form.field.notfound"));
        }
    }
}
