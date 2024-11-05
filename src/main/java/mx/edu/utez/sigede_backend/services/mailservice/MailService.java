package mx.edu.utez.sigede_backend.services.mailservice;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import mx.edu.utez.sigede_backend.controllers.mailcontroller.DTO.UserIdAndLogoDTO;
import mx.edu.utez.sigede_backend.models.mail.MailDesigns;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.models.user_account.UserAccountRepository;
import mx.edu.utez.sigede_backend.models.verification_code.VerificationCode;
import mx.edu.utez.sigede_backend.models.verification_code.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    MailDesigns mailDesigns;
    @Transactional
    public void sendEmail(String email,String subject,String body)throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(body,true);
        mailSender.send(message);
    }

    @Transactional
    public void sendPasswordResetCode(String email)throws MessagingException{

        UserIdAndLogoDTO userIdAndLogoDTO=userAccountRepository.findIdAndLogoByEmail(email);
        if(userIdAndLogoDTO==null){
            throw new IllegalArgumentException("Usuario no encontrado con el email proporcionado");
        }
        UserAccount userAccount=new UserAccount(userIdAndLogoDTO.getUserId());

        VerificationCode code = new VerificationCode();
        code.setVerificationCode(UUID.randomUUID().toString());
        code.setFkUserAccount(userAccount);
        code.setCreatedAt(LocalDateTime.now());
        code.setExpiration(LocalDateTime.now().plusMinutes(10));
        verificationCodeRepository.save(code);

        sendEmail(email,"Codigo de recuperación",mailDesigns.sendCodeVerificationDesign(code.getVerificationCode(),userIdAndLogoDTO.getLogo()));
    }


    @Transactional
    public void sendTemporaryPassword(String email) throws MessagingException{
        UserAccount newAdmin = userAccountRepository.findByEmail(email);
        if(newAdmin==null){
            throw new IllegalArgumentException("Usuario no encontrado con el email proporcionado");
        }
        String temPasword = UUID.randomUUID().toString().substring(8);
        newAdmin.setPassword(temPasword);
        userAccountRepository.save(newAdmin);
        sendEmail(email,"Bienvenido Nuevo Administrador",mailDesigns.sendTemporaryPasswordDesign(temPasword,newAdmin.getFkInstitution().getLogo()));
    }
    @Transactional
    public boolean validateResetCode(String email){
        try {
            UserAccount user = userAccountRepository.findByEmail(email);
            if(user == null){
                throw new IllegalArgumentException("Usuario no encontrado con el email proporcionado");
            }
            VerificationCode verificationCode = verificationCodeRepository.findByFkUserAccount(user);
            return verificationCode!=null&&verificationCode.getExpiration().isAfter(LocalDateTime.now());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            System.out.println("Funcion validateResetCode finalizado");
        }
    }

}
