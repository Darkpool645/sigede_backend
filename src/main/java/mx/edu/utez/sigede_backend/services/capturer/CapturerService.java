package mx.edu.utez.sigede_backend.services.capturer;

import lombok.extern.slf4j.Slf4j;
import mx.edu.utez.sigede_backend.controllers.capturers.dto.RequestCapturerRegistrationDTO;
import mx.edu.utez.sigede_backend.controllers.capturers.dto.RequestUpdateBasicData;
import mx.edu.utez.sigede_backend.controllers.capturers.dto.ResponseCapturistDTO;
import mx.edu.utez.sigede_backend.models.capturist_profile.CapturistProfile;
import mx.edu.utez.sigede_backend.models.capturist_profile.CapturistProfileRepository;
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

@Slf4j
@Service
public class CapturerService {
    private final UserAccountRepository userAccountRepository;
    private final CapturistProfileRepository capturistProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final StatusRepository statusRepository;
    private final InstitutionRepository institutionRepository;
    private final MailService mailService;
    private static final String USER_FOUND = "capturer.email.error";

    public CapturerService(UserAccountRepository userAccountRepository, CapturistProfileRepository capturistProfileRepository,
                           PasswordEncoder passwordEncoder, RolRepository rolRepository, StatusRepository statusRepository,
                           InstitutionRepository institutionRepository, MailService mailService) {
        this.userAccountRepository = userAccountRepository;
        this.capturistProfileRepository = capturistProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
        this.statusRepository = statusRepository;
        this.institutionRepository = institutionRepository;
        this.mailService = mailService;
    }

    @Transactional
    public ResponseCapturistDTO getOneCapturer(Long userId, Long institutionId) {
        Institution institution = this.institutionRepository.findByInstitutionId(institutionId);
        if (institution == null) {
            throw new CustomException("user.not.found");
        }
        UserAccount user = userAccountRepository.findByUserAccountIdAndFkInstitutionAndRolName(userId, institutionId,"capturista");
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
public Long getCapturistIdByEmail(String email) {
        return userAccountRepository.findUserAccountIdByEmail(email);
}



    @Transactional
    public Page<UserAccount> getCapturistByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Rol rol = rolRepository.findByName("CAPTURIST");
        return userAccountRepository.findByNameContainingIgnoreCaseAndFkRol(name, rol, pageable);
    }

    @Transactional
    public Page<UserAccount> getCapturistsByNameAndInstitution(String name, Long id, int page, int size) {
        Institution institution = institutionRepository.findByInstitutionId(id);
        Rol rol = rolRepository.findByName("CAPTURIST");
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return userAccountRepository.findByNameContainingIgnoreCaseAndFkInstitutionAndFkRol(
                name, institution, rol, pageable
        );
    }

    @Transactional
    public void registerCapturer(RequestCapturerRegistrationDTO payload) {
        if (userAccountRepository.findByEmail(payload.getEmail()) != null) {
            throw new CustomException(USER_FOUND);
        }
        Rol rol = rolRepository.findByName("CAPTURIST");
        if (rol == null) {
            throw new CustomException("rol.notfound");
        }

        Status status = statusRepository.findByName("activo");
        if (status == null) {
            throw new CustomException("status.notfound");
        }
        Institution institution = institutionRepository.findByInstitutionId(payload.getFkInstitution());
        if (institution == null) {
            throw new CustomException("institution.notfound");
        }
        // Crear una nueva cuenta de usuario
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
        //Mandar codigo al correo

        mailService.sendTemporaryPassword(userAccount.getEmail(), "Registro existoso", temporaryPassword, "Capturista");
        // Crear un perfil de capturista
        CapturistProfile capturistProfile = new CapturistProfile();
        capturistProfile.setFkProfile(userAccount);
        capturistProfileRepository.save(capturistProfile);
    }

    @Transactional
    public boolean changeCapturistStatus(Long userId) {
        try {
            UserAccount user = userAccountRepository.findByUserAccountId(userId);
            if (user == null) {
                throw new CustomException("user.not.found");
            }
            Status status;
            if (user.getFkStatus().getName().equals("activo")) {
                status = statusRepository.findByName("inactivo");
            } else {
                status = statusRepository.findByName("activo");
            }
            user.setFkStatus(status);
            userAccountRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error("Error al cambiar el estado del usuario", e);
            return false;
        }
    }


    @jakarta.transaction.Transactional
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
            log.error("Error al cambiar el estado del usuario", e);
            return false;
        }
    }
}
