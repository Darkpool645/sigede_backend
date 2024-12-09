package mx.edu.utez.sigede_backend.services.admin;

import mx.edu.utez.sigede_backend.controllers.admin.dto.RequestNewAdminDTO;
import mx.edu.utez.sigede_backend.controllers.admin.dto.RequestUpdateBasicData;
import mx.edu.utez.sigede_backend.controllers.capturers.dto.ResponseCapturistDTO;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;
import mx.edu.utez.sigede_backend.models.rol.Rol;
import mx.edu.utez.sigede_backend.models.rol.RolRepository;
import mx.edu.utez.sigede_backend.models.status.Status;
import mx.edu.utez.sigede_backend.models.status.StatusRepository;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.models.user_account.UserAccountRepository;
import mx.edu.utez.sigede_backend.services.mailservice.MailService;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.helpers.RandomPasswordGenerate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminService {
    private final UserAccountRepository userAccountRepository;
    private final RolRepository rolRepository;
    private final StatusRepository statusRepository;
    private final InstitutionRepository institutionRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public AdminService(UserAccountRepository userAccountRepository, RolRepository rolRepository, StatusRepository statusRepository, InstitutionRepository institutionRepository, MailService mailService, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.rolRepository = rolRepository;
        this.statusRepository = statusRepository;
        this.institutionRepository = institutionRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Page<UserAccount> getAdminsByNameAndInstitution(String name, Long institutionId, int page, int size) {
        Institution institution = institutionRepository.findByInstitutionId(institutionId);

        if (institution == null) {
            throw new CustomException("institution.notfound");
        }

        Rol rol = rolRepository.findByNameIgnoreCase("ADMIN");
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        return userAccountRepository.findByNameContainingIgnoreCaseAndFkInstitutionAndFkRol(name, institution, rol, pageable);
    }

    @Transactional
    public void registerAdmin(RequestNewAdminDTO payload){
        if(userAccountRepository.existsByEmail(payload.getEmail())){
            throw new CustomException("admin.email.error");
        }

        Rol rol = rolRepository.findByNameIgnoreCase("admin");
        if(rol == null){
            throw new CustomException("rol.notfound");
        }
        Status status = statusRepository.findByName("activo");
        if(status == null){
            throw new CustomException("status.notfound");
        }

        Institution institution = institutionRepository.findByInstitutionId(payload.getFkInstitution());
        if(institution == null){
            throw new CustomException("institution.notfound");
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(payload.getEmail());
        RandomPasswordGenerate randomPasswordGenerate = new RandomPasswordGenerate();
        String temporaryPassword = randomPasswordGenerate.generatedRandomPassword();
        userAccount.setName(payload.getName());
        userAccount.setPassword(passwordEncoder.encode(temporaryPassword));
        userAccount.setFkRol(rol);
        userAccount.setFkStatus(status);
        userAccount.setFkInstitution(institution);
        userAccountRepository.save(userAccount);

        mailService.sendTemporaryPassword(userAccount.getEmail(), "Registro exitoso",temporaryPassword,"Administrador");
    }

    @Transactional
    public ResponseCapturistDTO getOneAdmin(Long userId, Long institutionId) {
        Institution institution = this.institutionRepository.findByInstitutionId(institutionId);
        if (institution == null) {
            throw new CustomException("user.not.found");
        }
        UserAccount user = userAccountRepository.findByUserAccountIdAndFkInstitutionAndRolName(userId, institutionId,"admin");

        if (user == null) {
            throw new CustomException("user.not.found");
        }
        ResponseCapturistDTO response = new ResponseCapturistDTO();
        response.setUserAccountId(user.getUserAccountId());
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setStatus(user.getFkStatus().getName());
        return response;
    }


    @Transactional
    public boolean updateBasicData(RequestUpdateBasicData payload) {
        try {
            UserAccount user = userAccountRepository.findByUserAccountId(payload.getUserAccountId());
            if (user == null) {
                throw new CustomException("user.not.found");
            }
            Status status = statusRepository.findByName(payload.getStatus());
            if (status == null) {
                throw new CustomException("status.not.found");
            }
            if (this.userAccountRepository.existsByEmailAndNotUserAccountId(payload.getEmail(), payload.getUserAccountId())) {
                throw new CustomException("email.already.exists");
            }
            user.setName(payload.getName());
            user.setEmail(payload.getEmail());
            user.setFkStatus(status);
            userAccountRepository.save(user);
            return true;
        } catch (Exception e) {
            System.err.println("Error al cambiar el estado del usuario:"+ e);
            return false;
        }
    }
}
