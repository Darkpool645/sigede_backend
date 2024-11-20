package mx.edu.utez.sigede_backend.services.credentials;

import jakarta.transaction.Transactional;
import mx.edu.utez.sigede_backend.controllers.credential_field.dto.RequestCredentialFieldDTO;
import mx.edu.utez.sigede_backend.controllers.credentials.DTO.GetCredentialsDTO;
import mx.edu.utez.sigede_backend.controllers.credentials.DTO.RequestCredentialDTO;
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
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
import org.apache.coyote.Request;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialFieldRepository credentialFieldRepository;
    private final UserInfoRepository userInfoRepository;
    private final InstitutionRepository institutionRepository;
    private final CredentialRepository credentialRepository;
    private final UserAccountRepository userAccountRepository;
    private final ErrorDictionary errorDictionary;

    public CredentialService(CredentialFieldRepository credentialFieldRepository, UserInfoRepository userInfoRepository, InstitutionRepository institutionRepository, CredentialRepository credentialRepository, UserAccountRepository userAccountRepository, ErrorDictionary errorDictionary) {
        this.credentialFieldRepository = credentialFieldRepository;
        this.userInfoRepository = userInfoRepository;
        this.institutionRepository = institutionRepository;
        this.credentialRepository = credentialRepository;
        this.userAccountRepository = userAccountRepository;
        this.errorDictionary = errorDictionary;
    }

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
}