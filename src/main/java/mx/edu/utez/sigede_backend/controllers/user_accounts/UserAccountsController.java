package mx.edu.utez.sigede_backend.controllers.user_accounts;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import mx.edu.utez.sigede_backend.controllers.user_accounts.dto.RequestEditStatusDTO;
import mx.edu.utez.sigede_backend.controllers.user_accounts.dto.ResponseOneAccountDTO;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.services.user_accounts.UserAccountService;
@RestController
@RequestMapping("/api/users") 
public class UserAccountsController {
    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("/")
    public ResponseEntity<List<UserAccount>> getAllUserAccounts() {
        List<UserAccount> userAcounts = userAccountService.getAllUserAccounts();
        return new ResponseEntity<>(userAcounts, HttpStatus.OK);
    }

    @GetMapping("/admins")
    public ResponseEntity<List<UserAccount>> getAllAdmins() {
        List<UserAccount> admins = userAccountService.getAllAdmins();
        return new ResponseEntity<>(admins, HttpStatus.OK);

    }

    @GetMapping("/administrators/{institutionId}")
    public List<UserAccount> getAdministratorsByInstitution(@PathVariable Long institutionId) {
        return userAccountService.getAdministratorsByInstitution(institutionId);
    }

    @PostMapping("/get-account")
    public CustomResponse<UserAccount> getUserAccountById(@Validated @RequestBody ResponseOneAccountDTO payload){
        UserAccount account = userAccountService.getUserAccountById(payload.getUserId());
        return new CustomResponse<>(200,"Estado actualizado correctamente",false,account);
    }

    @PostMapping("/update-status")
    public CustomResponse<String> updateStatusAdmin(@Validated @RequestBody RequestEditStatusDTO payload){
        userAccountService.updateStatus(payload);
        return new CustomResponse<>(200,"Estado actualizado correctamente",false,null);
    }
}
