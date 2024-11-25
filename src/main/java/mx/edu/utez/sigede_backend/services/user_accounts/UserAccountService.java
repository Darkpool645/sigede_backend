package mx.edu.utez.sigede_backend.services.user_accounts;

import java.util.List;
import jakarta.transaction.Transactional;
import mx.edu.utez.sigede_backend.controllers.user_accounts.dto.*;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;
import mx.edu.utez.sigede_backend.models.rol.Rol;
import mx.edu.utez.sigede_backend.models.rol.RolRepository;
import mx.edu.utez.sigede_backend.models.status.Status;
import mx.edu.utez.sigede_backend.models.status.StatusRepository;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import mx.edu.utez.sigede_backend.controllers.capturers.dto.GetCapturistsDTO;

import org.springframework.stereotype.Service;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.models.user_account.UserAccountRepository;

@Service
public class UserAccountService {
    private final UserAccountRepository userAccountRepository;
    private final StatusRepository statusRepository;
    private final RolRepository rolRepository;
    private final InstitutionRepository institutionRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserAccountRepository userAccountRepository, StatusRepository statusRepository,
                              RolRepository rolRepository, InstitutionRepository institutionRepository,
                              PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.statusRepository = statusRepository;
        this.rolRepository = rolRepository;
        this.institutionRepository = institutionRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public List<UserAccount> getAllUserAccounts() {
        return userAccountRepository.getAllUserAccounts();
    }

    public List<GetCapturistsDTO> getAllAdmins() {
        return userAccountRepository.getAllAdmins("admin");
    }

    @Transactional
    public ResponseGetAccountDTO getUserAccountById (Long id){
        UserAccount user = userAccountRepository.findByUserAccountId(id);
        if(user == null){
            throw new CustomException("user.not.found");
        }
        return new ResponseGetAccountDTO(user.getEmail(),user.getName(),user.getFkRol().getName(),user.getFkStatus().getName(),user.getFkInstitution().getInstitutionId());

    }

    public List<UserAccount> getAdministratorsByInstitution(Long institutionId) {
        return userAccountRepository.findAdministratorsByInstitution(institutionId);
    }
    @Transactional
    public Page<ResponseAllAdminByInstitutionDTO> getAllAccountByInstitutionByRole (RequestAllAdminByInstitutionDTO payload, Pageable pageable){
        Rol role = rolRepository.findByName(payload.getRole());
        if(role == null){
            throw new CustomException("rol.notfound");
        }

        Institution institution = institutionRepository.findByInstitutionId(payload.getInstitutionId());
        if(institution == null){
            throw new CustomException("institution.notfound");
        }
        Page<UserAccount> accounts =  userAccountRepository.findAllByFkRol_NameAndFkInstitution_InstitutionId(role.getName(), institution.getInstitutionId(),pageable);
        return accounts.map(account -> {
            ResponseAllAdminByInstitutionDTO dto = new ResponseAllAdminByInstitutionDTO();
            dto.setUserId(account.getUserAccountId());
            dto.setEmail(account.getEmail());
            dto.setName(account.getName());
            return dto;
        });
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
    @Transactional
    public void updateData (RequestEditDataDTO payload){
        UserAccount userAccount = userAccountRepository.findById(payload.getUserId())
                .orElseThrow(() -> new CustomException("user.not.found"));

        if (payload.getPassword() != null) {
            userAccount.setEmail(passwordEncoder.encode(payload.getPassword()));
        }
        if (payload.getName() != null) {
            userAccount.setName(payload.getName());
        }
        if (payload.getFkRol() != null) {
            Rol rol = rolRepository.findById(payload.getFkRol())
                    .orElseThrow(() -> new CustomException("rol.notfound"));
            userAccount.setFkRol(rol);
        }
        if (payload.getFkStatus() != null) {
            Status status = statusRepository.findById(payload.getFkStatus())
                    .orElseThrow(() -> new CustomException("status.notfound"));
            userAccount.setFkStatus(status);
        }
        userAccountRepository.save(userAccount);
    }

    public List<GetCapturistsDTO> getCapturistasByInstitution(Long institutionId) {
        List<GetCapturistsDTO> capturistas = userAccountRepository.findCapturistasByInstitution(institutionId);

        if (capturistas.isEmpty()) {
            throw new CustomException("institution.notfound");
        }

        return capturistas;
    }

}
