package mx.edu.utez.sigede_backend.services.credentials;

import mx.edu.utez.sigede_backend.controllers.credentials.DTO.GetCredentialsDTO;
import mx.edu.utez.sigede_backend.models.credential.CredentialRepository;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.models.user_account.UserAccountRepository;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private final CredentialRepository credentialRepository;
    private final UserAccountRepository userAccountRepository;
    private final ErrorDictionary errorDictionary;

    public CredentialService(CredentialRepository credentialRepository, UserAccountRepository userAccountRepository, ErrorDictionary errorDictionary) {
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
}