package mx.edu.utez.sigede_backend.services.password_recovery;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.PasswordChangeResponseDTO;
import mx.edu.utez.sigede_backend.services.mailservice.MailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.models.user_account.UserAccountRepository;
import mx.edu.utez.sigede_backend.models.verification_code.VerificationCode;
import mx.edu.utez.sigede_backend.models.verification_code.VerificationCodeRepository;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;

@Service
public class PasswordRecoveryService {
    private final UserAccountRepository userAccountRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private static final String VERIFICATION_CODE = "Código de verificación";
    private static final SecureRandom random = new SecureRandom();
    private static final int CODE_LENGTH = 6;
    private static final String USER_NOT_FOUND = "user.not.found";

    public PasswordRecoveryService(UserAccountRepository userAccountRepository,
            VerificationCodeRepository verificationCodeRepository, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userAccountRepository = userAccountRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    @Transactional
    public Long sendVerificationCode(String email) {
        if (!userAccountRepository.existsByEmail(email)) {
            throw new CustomException(USER_NOT_FOUND);
        }
        UserAccount user = userAccountRepository.findByEmail(email);
        VerificationCode verificationCode = verificationCodeRepository.findByFkUserAccount(user);

        if (verificationCode == null) {
            verificationCode = new VerificationCode();
            verificationCode.setFkUserAccount(user);
        }

        String code = generateVerificationCode();
        verificationCode.setVerificationCode(code);
        verificationCode.setCreatedAt(LocalDateTime.now());
        verificationCode.setExpiration(LocalDateTime.now().plusHours(1));
        verificationCodeRepository.saveAndFlush(verificationCode);
        mailService.sendVerificationCodeEmail(email, VERIFICATION_CODE, code);
        return user.getUserAccountId();
    }

    @Transactional
    public boolean validateVerificationCode(String code, Long userId) {
        if (!userAccountRepository.existsByUserAccountId(userId)) {
            throw new CustomException(USER_NOT_FOUND);
        }
        UserAccount user = userAccountRepository.findByUserAccountId(userId);
        VerificationCode databaseCode = verificationCodeRepository.findByFkUserAccount(user);
        if (LocalDateTime.now().isAfter(databaseCode.getExpiration())) {
            throw new CustomException("verification.code.expired");
        }
        return databaseCode.getVerificationCode().equals(code);
    }

    @Transactional
    public PasswordChangeResponseDTO changePassword(String newPassword, Long userId) {

        if (!userAccountRepository.existsByUserAccountId(userId)) {
            throw new CustomException(USER_NOT_FOUND);
        }
        UserAccount user = userAccountRepository.findByUserAccountId(userId);
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new CustomException("user.password.same_as_old");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userAccountRepository.save(user);
        mailService.sendPasswordChangeEmail(user.getEmail(), "Contraseña actualizada");
        return new PasswordChangeResponseDTO(user.getPassword(), user.getUserAccountId());
    }

    @Transactional
    public Long resendVerificationCode(String email) {
        UserAccount user = userAccountRepository.findByEmail(email);
        if (user == null) {
            throw new CustomException(USER_NOT_FOUND);
        }

        VerificationCode verificationCode = verificationCodeRepository.findByFkUserAccount(user);
        String code = generateVerificationCode();
        verificationCode.setVerificationCode(code);
        verificationCode.setCreatedAt(LocalDateTime.now());
        verificationCode.setExpiration(LocalDateTime.now().plusHours(1));
        verificationCodeRepository.saveAndFlush(verificationCode);
        mailService.sendVerificationCodeEmail(email, VERIFICATION_CODE, code);
        return user.getUserAccountId();
    }

    @Transactional
    public void deleteVerificationCode(Long userId) {
        UserAccount user = userAccountRepository.findByUserAccountId(userId);
        verificationCodeRepository.deleteByFkUserAccount(user);
    }

    private static String generateVerificationCode() {
        int min = (int) Math.pow(10, CODE_LENGTH - 1);
        int max = (int) Math.pow(10, CODE_LENGTH) - 1;
        int code = random.nextInt((max - min) + 1) + min;
        return String.valueOf(code);
    }
}
