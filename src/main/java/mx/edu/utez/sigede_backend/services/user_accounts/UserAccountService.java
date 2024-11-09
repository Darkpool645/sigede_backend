package mx.edu.utez.sigede_backend.services.user_accounts;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.models.user_account.UserAccountRepository;
@Service
public class UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    public List<UserAccount> getAllUserAccounts() {
        return userAccountRepository.getAllUserAccounts();
    }

    public List<UserAccount> getAllAdmins() {
        return userAccountRepository.getAllAdmins("Admin");
    }
    
    public Optional<UserAccount> getUserAccountById(Long id) {
        return userAccountRepository.findById(id);
    }

    public List<UserAccount> getAdministratorsByInstitution(UUID institutionId) {
        return userAccountRepository.findAdministratorsByInstitution(institutionId);
    }

}
