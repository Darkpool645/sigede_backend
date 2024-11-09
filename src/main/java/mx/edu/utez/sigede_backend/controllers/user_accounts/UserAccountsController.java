package mx.edu.utez.sigede_backend.controllers.user_accounts;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{id}")
    public ResponseEntity<UserAccount> getUserAccountById(@PathVariable Long id) {
        Optional<UserAccount> userAccount = userAccountService.getUserAccountById(id);
        return userAccount.map(ResponseEntity::ok)
                          .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
