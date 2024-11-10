package mx.edu.utez.sigede_backend.services.capturer;

import jakarta.transaction.Transactional;
import mx.edu.utez.sigede_backend.controllers.capturers.dto.RequestCapturerRegistrationDTO;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

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
    public CapturerService(UserAccountRepository userAccountRepository, CapturistProfileRepository capturistProfileRepository, PasswordEncoder passwordEncoder, RolRepository rolRepository, StatusRepository statusRepository, InstitutionRepository institutionRepository, MailService mailService) {
        this.userAccountRepository = userAccountRepository;
        this.capturistProfileRepository = capturistProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
        this.statusRepository = statusRepository;
        this.institutionRepository = institutionRepository;
        this.mailService = mailService;
    }
    @Transactional
    public void registerCapturer(RequestCapturerRegistrationDTO payload) {
        if(userAccountRepository.findByEmail(payload.getEmail())!=null){
            throw new CustomException(USER_FOUND);
        }
        Rol rol = rolRepository.findByName("capturista");
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

        mailService.sendTemporaryPassword(userAccount.getEmail(), "Registro existoso", temporaryPassword,"Capturista");
        // Crear un perfil de capturista
        CapturistProfile capturistProfile = new CapturistProfile();
        capturistProfile.setFkProfile(userAccount);
        capturistProfileRepository.save(capturistProfile);
    }


}
