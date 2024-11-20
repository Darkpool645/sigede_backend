package mx.edu.utez.sigede_backend.services.user_info;

import mx.edu.utez.sigede_backend.controllers.user_info.DTO.UserInfoDTO;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;
import mx.edu.utez.sigede_backend.models.institution_capturist_field.InstitutionCapturistField;
import mx.edu.utez.sigede_backend.models.institution_capturist_field.InstitutionCapturistFieldRepository;
import mx.edu.utez.sigede_backend.models.user_info.UserInfo;
import mx.edu.utez.sigede_backend.models.user_info.UserInfoRepository;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserInfoService {
    private final InstitutionCapturistFieldRepository institutionCapturistFieldRepository;
    private final UserInfoRepository userInfoRepository;
    private final InstitutionRepository institutionRepository;
    private final ErrorDictionary errorDictionary;

    public UserInfoService(
            InstitutionCapturistFieldRepository institutionCapturistFieldRepository,
            UserInfoRepository userInfoRepository,
            InstitutionRepository institutionRepository,
            ErrorDictionary errorDictionary) {
        this.institutionCapturistFieldRepository = institutionCapturistFieldRepository;
        this.userInfoRepository = userInfoRepository;
        this.institutionRepository = institutionRepository;
        this.errorDictionary = errorDictionary;
    }

    public InstitutionCapturistField createFieldAndAssociate(UserInfoDTO userInfoDTO, Long institutionId, boolean isRequired) {
        Optional<Institution> institution = institutionRepository.findById(institutionId);
        if (institution.isEmpty()) {
            throw new CustomException("institution.notfound");
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setTag(userInfoDTO.getTag());
        userInfo.setType(userInfoDTO.getType());
        userInfo.setInQr(userInfoDTO.isInQr());
        userInfo.setInCard(userInfoDTO.isInCard());
        userInfo = userInfoRepository.save(userInfo);

        InstitutionCapturistField capturistField = new InstitutionCapturistField();
        capturistField.setFkInstitution(institution.get());
        capturistField.setFkUserInfo(userInfo);
        capturistField.setRequired(isRequired);

        return institutionCapturistFieldRepository.save(capturistField);
    }

    public InstitutionCapturistField updateCapturistField(Long fieldId, boolean isRequired, UserInfoDTO userInfoDTO) {
        Optional<InstitutionCapturistField> existingField = institutionCapturistFieldRepository.findById(fieldId);
        if (existingField.isEmpty()) {
            throw new CustomException("form.field.notfound");
        }

        InstitutionCapturistField field = existingField.get();

        field.setRequired(isRequired);

        UserInfo userInfo = field.getFkUserInfo();
        userInfo.setTag(userInfoDTO.getTag());
        userInfo.setType(userInfoDTO.getType());
        userInfo.setInQr(userInfoDTO.isInQr());
        userInfo.setInCard(userInfoDTO.isInCard());
        userInfoRepository.save(userInfo);

        return institutionCapturistFieldRepository.save(field);
    }


    public List<InstitutionCapturistField> getFieldsByInstitution(Long institutionId) {
        return institutionCapturistFieldRepository.findAllByFkInstitution_InstitutionId(institutionId);
    }
}
