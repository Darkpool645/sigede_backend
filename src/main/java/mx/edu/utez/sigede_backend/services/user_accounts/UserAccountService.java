package mx.edu.utez.sigede_backend.services.user_accounts;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import jakarta.transaction.Transactional;
import mx.edu.utez.sigede_backend.controllers.user_accounts.dto.RequestEditStatusDTO;
import mx.edu.utez.sigede_backend.models.rol.RolRepository;
import mx.edu.utez.sigede_backend.models.status.Status;
import mx.edu.utez.sigede_backend.models.status.StatusRepository;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.models.user_account.UserAccountRepository;
@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final StatusRepository statusRepository;

    public UserAccountService(UserAccountRepository userAccountRepository, StatusRepository statusRepository) {
        this.userAccountRepository = userAccountRepository;
        this.statusRepository = statusRepository;
    }

    public List<UserAccount> getAllUserAccounts() {
        return userAccountRepository.getAllUserAccounts();
    }

    public List<UserAccount> getAllAdmins() {
        return userAccountRepository.getAllAdmins("admin");
    }

    @Transactional
    public UserAccount getUserAccountById (Long id){
        UserAccount user = userAccountRepository.findByUserAccountId(id);
        if(user == null){
            throw new CustomException("user.not.found");
        }
        return user;
    }

    public List<UserAccount> getAdministratorsByInstitution(Long institutionId) {
        return userAccountRepository.findAdministratorsByInstitution(institutionId);
    }

    @Transactional
    public void updateStatus(RequestEditStatusDTO payload){
        UserAccount userId = userAccountRepository.findByEmail(payload.getEmail());
        if(userId == null){
            throw new CustomException("user.not.found");
        }
        if(!userId.getFkStatus().getName().equals(payload.getStatus())){
            throw new CustomException("status.same");
        }
        Status status = statusRepository.findByName(payload.getStatus());
        if(status == null){
            throw new CustomException("status.notfound");
        }


        Status activo = statusRepository.findByName("activo");
        if(activo == null){
            throw new CustomException("status.notfound");
        }
        Status inactivo = statusRepository.findByName("inactivo");
        if(inactivo == null){
            throw new CustomException("status.notfound");
        }

        switch (status.getName()){
            case "activo":
                userId.setFkStatus(inactivo);
                userAccountRepository.saveAndFlush(userId);
                break;
            case "inactivo":
                userId.setFkStatus(activo);
                userAccountRepository.saveAndFlush(userId);
                break;
            default:
                throw new CustomException("opcion invalida");
        }
    }

}
