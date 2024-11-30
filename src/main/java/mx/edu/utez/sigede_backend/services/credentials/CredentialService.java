package mx.edu.utez.sigede_backend.services.credentials;

import mx.edu.utez.sigede_backend.controllers.credential_field.dto.RequestCredentialFieldDTO;
import mx.edu.utez.sigede_backend.controllers.credential_field.dto.RequestUpdateCredentialFieldDTO;
import mx.edu.utez.sigede_backend.controllers.credential_field.dto.ResponseCredentialFieldDTO;
import mx.edu.utez.sigede_backend.controllers.credentials.DTO.*;
import mx.edu.utez.sigede_backend.models.credential.Credential;
import mx.edu.utez.sigede_backend.models.credential.CredentialRepository;
import mx.edu.utez.sigede_backend.models.credential_field.CredentialField;
import mx.edu.utez.sigede_backend.models.credential_field.CredentialFieldRepository;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.models.user_account.UserAccountRepository;
import mx.edu.utez.sigede_backend.models.user_info.UserInfo;
import mx.edu.utez.sigede_backend.models.user_info.UserInfoRepository;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CredentialService {
    private final CredentialFieldRepository credentialFieldRepository;
    private final UserInfoRepository userInfoRepository;
    private final InstitutionRepository institutionRepository;
    private final CredentialRepository credentialRepository;
    private final UserAccountRepository userAccountRepository;

    public CredentialService(CredentialFieldRepository credentialFieldRepository, UserInfoRepository userInfoRepository,
                             InstitutionRepository institutionRepository, CredentialRepository credentialRepository,
                             UserAccountRepository userAccountRepository) {
        this.credentialFieldRepository = credentialFieldRepository;
        this.userInfoRepository = userInfoRepository;
        this.institutionRepository = institutionRepository;
        this.credentialRepository = credentialRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
    public List<GetCredentialsDTO> getCredentialsByCapturerId(Long userAccountId) {
        UserAccount userAccount = userAccountRepository.findById(userAccountId)
                .orElseThrow(() -> new CustomException("user.not.found"));

        if (!"capturista".equalsIgnoreCase(userAccount.getFkRol().getName())) {
            throw new CustomException("user.role.invalid");
        }

        List<GetCredentialsDTO> credentials = credentialRepository.findCredentialsByUserAccountId(userAccountId);
        if (credentials.isEmpty()) {
            throw new CustomException("credentials.notfound");
        }
        return credentials;
    }

    @Transactional
    public void createCredential(RequestCredentialDTO payload) {
        // Verificar si la institución existe
        Institution institution = institutionRepository.findById(payload.getInstitutionId())
                .orElseThrow(() -> new CustomException("institution.notfound"));

        // Verificar si el usuario existe
        UserAccount userAccount = userAccountRepository.findById(payload.getUserAccountId())
                .orElseThrow(() -> new CustomException("user.not.found"));

        // Crear la nueva credencial
        Credential credential = new Credential();
        credential.setFullname(payload.getFullname());
        credential.setUserPhoto(payload.getUserPhoto());
        credential.setIssueDate(LocalDateTime.now());
        credential.setFkInstitution(institution);
        credential.setFkUserAccount(userAccount);
        credential.setExpirationDate(LocalDateTime.of(2025, 12, 31, 23, 59, 59));

        credential = credentialRepository.save(credential);

        for (RequestCredentialFieldDTO fieldDTO : payload.getFields()) {
            // Se asume que los tags ya están definidos en la tabla 'UserInfo' y se encuentran asociados
            UserInfo userInfo = userInfoRepository.findByTag(fieldDTO.getTag());
            if (userInfo == null){
                throw new CustomException("user.info.not.found: " + fieldDTO.getTag());
            }

            CredentialField credentialField = new CredentialField();
            credentialField.setValue(fieldDTO.getValue());  // Asignamos el valor del campo
            credentialField.setFkCredential(credential);
            credentialField.setFkUserInfo(userInfo);

            credentialFieldRepository.save(credentialField); // Guardamos el CredentialField
        }
    }

    @Transactional
    public ResponseCredentialDTO getCredentialWithFields(Long credentialId) {
        if(credentialId == null){
            throw new CustomException("credentialId.not.null");
        }
        // Obtener la credencial
        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new CustomException("credential.not.found"));

        // Obtener los CredentialFields relacionados
        List<CredentialField> credentialFields = credentialFieldRepository.findByCredentialId(credentialId);

        // Crear la lista de ResponseCredentialFieldDTO a partir de los CredentialFields
        List<ResponseCredentialFieldDTO> fieldDTOs = credentialFields.stream()
                .map(ResponseCredentialFieldDTO::new) // Usamos el constructor que convierte CredentialField a DTO
                .collect(Collectors.toList());

        // Crear y retornar el DTO principal con los datos
        return new ResponseCredentialDTO(
                credential.getCredentialId(),
                credential.getFullname(),
                credential.getUserPhoto(),
                credential.getExpirationDate(),
                credential.getIssueDate(),
                fieldDTOs
        );
    }

    @Transactional
    public void updateCredential (Long credentialId, RequestUpdateCredentialDTO payload){
        if(credentialId == null){
            throw new CustomException("credentialId.not.null");
        }
        // Obtener la credencial que se va a actualizar
        Credential credential = credentialRepository.findById(credentialId)
                .orElseThrow(() -> new CustomException("Credential not found"));

        // Actualizar los campos de la credencial
        credential.setFullname(payload.getFullname());
        credential.setUserPhoto(payload.getUserPhoto());
        credential.setExpirationDate(payload.getExpirationDate());
        credential.setIssueDate(payload.getIssueDate());

        // Guardar la credencial actualizada
        credentialRepository.save(credential);

        // Actualizar los CredentialField usando el tag
        for (RequestUpdateCredentialFieldDTO fieldDTO : payload.getFields()) {
            // Encontrar el UserInfo por el tag
            UserInfo userInfo = userInfoRepository.findByTag(fieldDTO.getTag());
            if(userInfo == null){
                throw new CustomException("UserInfo with tag " + fieldDTO.getTag() + " not found");
            }

            // Encontrar el CredentialField asociado a la credencial y al UserInfo
            CredentialField field = credentialFieldRepository.findByFkCredentialAndFkUserInfo(credential, userInfo)
                    .orElseThrow(() -> new CustomException("CredentialField for tag " + fieldDTO.getTag() + " not found"));

            // Actualizar el valor del campo
            field.setValue(fieldDTO.getValue());

            // Guardar el CredentialField actualizado
            credentialFieldRepository.save(field);
        }

    }



    @Transactional
    public Page<GetCretentialsByInstitutoIdDTO> getAllAccountByInstitution (RequestByInstitution payload, Pageable pageable){
        Institution institution=institutionRepository.findByInstitutionId(payload.getInstitutionId());
        if(institution == null){
            throw new CustomException("institution.notfound");
        }
        return credentialRepository.findAllByFkInstitutionAndFullname(
                institution.getInstitutionId(),
                payload.getFullname(),
                pageable
        );
    }
}