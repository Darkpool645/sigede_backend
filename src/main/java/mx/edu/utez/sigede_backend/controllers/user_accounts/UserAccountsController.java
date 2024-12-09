package mx.edu.utez.sigede_backend.controllers.user_accounts;

import java.util.List;

import mx.edu.utez.sigede_backend.controllers.user_accounts.dto.*;
import mx.edu.utez.sigede_backend.utils.CustomResponse;

import mx.edu.utez.sigede_backend.controllers.capturers.dto.GetCapturistsDTO;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.services.user_accounts.UserAccountService;

@RestController
@RequestMapping("/api/users") 
public class UserAccountsController {
    private final UserAccountService userAccountService;
    private final ErrorDictionary errorDictionary;

    public UserAccountsController(UserAccountService userAccountService, ErrorDictionary errorDictionary) {
        this.userAccountService = userAccountService;
        this.errorDictionary = errorDictionary;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserAccount>> getAllUserAccounts() {
        List<UserAccount> userAccounts = userAccountService.getAllUserAccounts();
        return new ResponseEntity<>(userAccounts, HttpStatus.OK);
    }

    @GetMapping("/admins/{institutionId}")
    public ResponseEntity<List<GetUserBasicInfoDTO>> getAllAdminsByInstitution(@PathVariable Long institutionId) {
        List<GetUserBasicInfoDTO> admins = userAccountService.getAllByRolNameAndInstitutionId(institutionId);

        return new ResponseEntity<>(admins, HttpStatus.OK);
    }

    @GetMapping("/administrators/{institutionId}")
    public List<UserAccount> getAdministratorsByInstitution(@PathVariable Long institutionId) {
        return userAccountService.getAdministratorsByInstitution(institutionId);
    }



    @PostMapping("/get-all-by-institution-rolename")
    public CustomResponse<Page<ResponseAllAdminByInstitutionDTO>> getAllByInstitutionByRole(@Validated @RequestBody RequestAllAdminByInstitutionDTO payload, Pageable pageable){
        Page<ResponseAllAdminByInstitutionDTO> data = userAccountService.getAllAccountByInstitutionByRole(payload,pageable);
        return new CustomResponse<>(200,"Usuarios",false,data);
    }

    @PostMapping("/get-account")
    public CustomResponse<ResponseGetAccountDTO> getUserAccountById(@Validated @RequestBody ResponseOneAccountDTO payload){
        ResponseGetAccountDTO account = userAccountService.getUserAccountById(payload.getUserId());
        return new CustomResponse<>(200,"Usuario",false,account);
    }

    @PostMapping("/update-status")
    public CustomResponse<String> updateStatusAdmin(@Validated @RequestBody RequestEditStatusDTO payload){
        userAccountService.updateStatus(payload);
        return new CustomResponse<>(200,"Estado actualizado correctamente",false,null);
    }

    @PostMapping("/update-data")
    public CustomResponse<String> updateData(@Validated @RequestBody RequestEditDataDTO payload){
        userAccountService.updateData(payload);
        return new CustomResponse<>(200,"Información actualizada correctamente",false,null);
    }

    @GetMapping("/capturists/{institutionId}")
    public ResponseEntity<?> getCapturistasByInstitution(@PathVariable Long institutionId) {
        try {
            List<GetCapturistsDTO> capturistas = userAccountService.getCapturistasByInstitution(institutionId);
            return new ResponseEntity<>(capturistas, HttpStatus.OK);
        } catch (CustomException e) {
            String errorMessage = errorDictionary.getErrorMessage(e.getErrorCode());
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/register-superadmin")
    public CustomResponse<String> registerSuperadmin(@Validated @RequestBody RequestRegisterSuperAdminDTO payload) {
        userAccountService.registerSuperAdmin(payload);
        return new CustomResponse<>(201, "Superadmin registrado correctamente", false, null);
    }
}
