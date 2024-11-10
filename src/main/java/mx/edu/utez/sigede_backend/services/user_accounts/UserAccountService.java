package mx.edu.utez.sigede_backend.services.user_accounts;
import java.util.List;
import java.util.Optional;

import mx.edu.utez.sigede_backend.controllers.capturers.dto.GetCapturistsDTO;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.models.user_account.UserAccountRepository;
@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private ErrorDictionary errorDictionary;

    public List<UserAccount> getAllUserAccounts() {
        return userAccountRepository.getAllUserAccounts();
    }

    public List<UserAccount> getAllAdmins() {
        return userAccountRepository.getAllAdmins("Admin");
    }
    
    public Optional<UserAccount> getUserAccountById(Long id) {
        return userAccountRepository.findById(id);
    }

    public List<UserAccount> getAdministratorsByInstitution(Long institutionId) {
        return userAccountRepository.findAdministratorsByInstitution(institutionId);
    }

    public List<GetCapturistsDTO> getCapturistasByInstitution(Long institutionId) {
        List<GetCapturistsDTO> capturistas = userAccountRepository.findCapturistasByInstitution(institutionId);

        if (capturistas.isEmpty()) {
            throw new CustomException("institution.notfound");
        }

        return capturistas;
    }

}
