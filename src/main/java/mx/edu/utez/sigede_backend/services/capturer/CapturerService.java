package mx.edu.utez.sigede_backend.services.capturer;

import jakarta.transaction.Transactional;
import mx.edu.utez.sigede_backend.controllers.capturers.dto.RequestCapturerRegistrationDTO;
import mx.edu.utez.sigede_backend.models.capturist_profile.CapturistProfile;
import mx.edu.utez.sigede_backend.models.capturist_profile.CapturistProfileRepository;
import mx.edu.utez.sigede_backend.models.rol.Rol;
import mx.edu.utez.sigede_backend.models.rol.RolRepository;
import mx.edu.utez.sigede_backend.models.status.Status;
import mx.edu.utez.sigede_backend.models.status.StatusRepository;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.models.user_account.UserAccountRepository;
import mx.edu.utez.sigede_backend.services.mailservice.MailService;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CapturerService {
    private final UserAccountRepository userAccountRepository;
    private final CapturistProfileRepository capturistProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolRepository rolRepository;
    private final StatusRepository statusRepository;
    private final MailService mailService;
    private static final String USER_FOUND = "capturer.email.error";
    public CapturerService(UserAccountRepository userAccountRepository, CapturistProfileRepository capturistProfileRepository, PasswordEncoder passwordEncoder, RolRepository rolRepository, StatusRepository statusRepository, MailService mailService) {
        this.userAccountRepository = userAccountRepository;
        this.capturistProfileRepository = capturistProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
        this.statusRepository = statusRepository;
        this.mailService = mailService;
    }
    @Transactional
    public void registerCapturer(RequestCapturerRegistrationDTO payload) {
        if(userAccountRepository.existsByEmail(payload.getEmail())){
            throw new CustomException(USER_FOUND);
        }
        Rol rol = rolRepository.findByName("capturista");
        if (rol == null) {
            throw new CustomException("Rol 'capturista' no encontrado");
        }

        Status status = statusRepository.findByName("activo");
        if (status == null) {
            throw new CustomException("Status 'activo' no encontrado");
        }

        // Crear una nueva cuenta de usuario
        UserAccount userAccount = new UserAccount();
        userAccount.setEmail(payload.getEmail());
        userAccount.setName(payload.getName());
        userAccount.setPassword(passwordEncoder.encode(payload.getPassword()));
        userAccount.setFkRol(rol);
        userAccount.setFkStatus(status);
        userAccountRepository.save(userAccount);
        //Mandar codigo al correo

        mailService.sendTemporaryPassword(userAccount.getEmail(), "", userAccount.getPassword());
        // Crear un perfil de capturista
        CapturistProfile capturistProfile = new CapturistProfile();
        capturistProfile.setFkProfile(userAccount);
        capturistProfileRepository.save(capturistProfile);
    }
}
