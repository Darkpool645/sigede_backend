package mx.edu.utez.sigede_backend.services.user_info;

import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.InstitutionResponseDTO;
import mx.edu.utez.sigede_backend.controllers.institution_capturist_field.DTO.InstitutionCapturistFieldDTO;
import mx.edu.utez.sigede_backend.controllers.user_info.DTO.UserInfoDTO;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;
import mx.edu.utez.sigede_backend.models.institution_capturist_field.InstitutionCapturistField;
import mx.edu.utez.sigede_backend.models.institution_capturist_field.InstitutionCapturistFieldRepository;
import mx.edu.utez.sigede_backend.models.user_info.UserInfo;
import mx.edu.utez.sigede_backend.models.user_info.UserInfoRepository;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserInfoService {
    private final InstitutionCapturistFieldRepository institutionCapturistFieldRepository;
    private final UserInfoRepository userInfoRepository;
    private final InstitutionRepository institutionRepository;

    public UserInfoService(
            InstitutionCapturistFieldRepository institutionCapturistFieldRepository,
            UserInfoRepository userInfoRepository,
            InstitutionRepository institutionRepository) {
        this.institutionCapturistFieldRepository = institutionCapturistFieldRepository;
        this.userInfoRepository = userInfoRepository;
        this.institutionRepository = institutionRepository;
    }

    @Transactional
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

    @Transactional
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


    @Transactional
    public Map<String, Object> getFieldsByInstitution(Long institutionId) {
        List<InstitutionCapturistField> fields = institutionCapturistFieldRepository.findAllByFkInstitution_InstitutionId(institutionId);

        if (fields.isEmpty()) {
            throw new CustomException("institution.notfound");
        }

        Institution institution = fields.get(0).getFkInstitution();
        InstitutionResponseDTO institutionDTO = new InstitutionResponseDTO(
                institution.getInstitutionId(),
                institution.getName(),
                institution.getInstitutionStatus()
        );

        List<InstitutionCapturistFieldDTO> capturistFieldDTOs = fields.stream()
                .map(field -> new InstitutionCapturistFieldDTO(
                        field.getInstitutionCapturistFieldId(),
                        field.isRequired(),
                        field.getFkInstitution().getInstitutionId(),
                        field.getFkUserInfo().getUserInfoId()

                ))
                .toList();

        List<UserInfoDTO> userInfoDTOs = fields.stream()
                .map(field -> {
                    UserInfo userInfo = field.getFkUserInfo();
                    return new UserInfoDTO(
                            userInfo.getUserInfoId(),
                            userInfo.getTag(),
                            userInfo.getType(),
                            userInfo.isInQr(),
                            userInfo.isInCard()
                    );
                })
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("institution", institutionDTO);
        response.put("fields", capturistFieldDTOs);
        response.put("userInfo", userInfoDTOs);
        return response;
    }
}
