package mx.edu.utez.sigede_backend.services.user_info;

import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.InstitutionResponseDTO;
import mx.edu.utez.sigede_backend.controllers.institution_capturist_field.DTO.InstitutionCapturistFieldDTO;
import mx.edu.utez.sigede_backend.controllers.user_info.DTO.UserInfoDTO;
import mx.edu.utez.sigede_backend.controllers.user_info.DTO.UserInfoPostDTO;
import mx.edu.utez.sigede_backend.controllers.user_info.DTO.UserInfoUpdateDTO;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;
import mx.edu.utez.sigede_backend.models.institution_capturist_field.InstitutionCapturistField;
import mx.edu.utez.sigede_backend.models.institution_capturist_field.InstitutionCapturistFieldRepository;
import mx.edu.utez.sigede_backend.models.user_info.UserInfo;
import mx.edu.utez.sigede_backend.models.user_info.UserInfoRepository;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    public List<InstitutionCapturistField> createFieldAndAssociate(
            List<UserInfoPostDTO> userInfoDTOs, Long institutionId) throws CustomException {

        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new CustomException("institution.notfound"));

        List<InstitutionCapturistField> fields = new ArrayList<>();

        for (UserInfoPostDTO userInfoDTO : userInfoDTOs) {
            UserInfo userInfo = new UserInfo();
            userInfo.setTag(userInfoDTO.getTag());
            userInfo.setType(userInfoDTO.getType());
            userInfo.setInQr(userInfoDTO.isInQr());
            userInfo.setInCard(userInfoDTO.isInCard());
            userInfo = userInfoRepository.save(userInfo);

            InstitutionCapturistField field = new InstitutionCapturistField();
            field.setFkInstitution(institution);
            field.setFkUserInfo(userInfo);
            field.setRequired(userInfoDTO.isRequired());

            fields.add(institutionCapturistFieldRepository.save(field));
        }

        return fields;
    }


    @Transactional
    public List<Map<String, Object>> updateCapturistFields(List<UserInfoUpdateDTO> updates) {
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (UserInfoUpdateDTO update : updates) {
            Optional<InstitutionCapturistField> optionalField = institutionCapturistFieldRepository.findById(update.getFieldId());
            if (optionalField.isEmpty()) {
                throw new CustomException("form.field.notfound");
            }

            InstitutionCapturistField field = optionalField.get();

            field.setRequired(update.getIsRequired());


            UserInfo userInfo = field.getFkUserInfo();
            userInfo.setTag(update.getTag());
            userInfo.setType(update.getType());
            userInfo.setInQr(update.isInQr());
            userInfo.setInCard(update.isInCard());

            userInfoRepository.save(userInfo);

            institutionCapturistFieldRepository.save(field);

            Map<String, Object> updatedFields = getStringObjectMap(update, field);

            responseList.add(updatedFields);
        }

        return responseList;
    }

    private static Map<String, Object> getStringObjectMap(UserInfoUpdateDTO update, InstitutionCapturistField field) {
        InstitutionResponseDTO institutionDTO = new InstitutionResponseDTO(
                field.getFkInstitution().getInstitutionId(),
                field.getFkInstitution().getName(),
                field.getFkInstitution().getInstitutionStatus()
        );

        Map<String, Object> updatedFields = new HashMap<>();
        updatedFields.put("isRequired", field.isRequired());
        updatedFields.put("tag", update.getTag());
        updatedFields.put("type", update.getType());
        updatedFields.put("inQr", update.isInQr());
        updatedFields.put("inCard", update.isInCard());
        updatedFields.put("institution", institutionDTO);
        return updatedFields;
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
