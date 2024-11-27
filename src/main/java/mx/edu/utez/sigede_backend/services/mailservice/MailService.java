package mx.edu.utez.sigede_backend.services.mailservice;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import mx.edu.utez.sigede_backend.models.mail.MailDesigns;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

import static mx.edu.utez.sigede_backend.utils.validations.RegexPatterns.EMAIL_REGEX;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    private final MailDesigns mailDesigns;

    public MailService(JavaMailSender mailSender, MailDesigns mailDesigns) {
        this.mailSender = mailSender;
        this.mailDesigns = mailDesigns;
    }

    @Async
    public void sendVerificationCodeEmail(String to, String subject, String verificationCode, String logo) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            if (to == null || to.isEmpty()) {
                throw new CustomException("user.email.notnull");
            } else if (!Pattern.matches(EMAIL_REGEX, to)) {
                throw new CustomException("user.email.invalid");
            } else {
                helper.setTo(to);
                helper.setSubject(subject);
                String html = mailDesigns.sendCodeVerificationDesign(verificationCode, logo);
                helper.setText(html, true);
                mailSender.send(mimeMessage);
            }
        } catch (MessagingException e) {
            throw new CustomException("email.send.error");
        }
    }

    @Async
    public void sendPasswordChangeEmail(String to, String subject, String logo) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            if (to == null || to.isEmpty()) {
                throw new CustomException("user.email.notnull");
            } else if (!Pattern.matches(EMAIL_REGEX, to)) {
                throw new CustomException("user.email.invalid");
            } else {
                helper.setTo(to);
                helper.setSubject(subject);
                String html = mailDesigns.sendPasswordChangeDesign(logo);
                helper.setText(html, true);
                mailSender.send(mimeMessage);
            }
        } catch (MessagingException e) {
            throw new CustomException("email.send.error");
        }
    }

    @Async
    public void sendTemporaryPassword(String to,String subject , String password,String role){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

        try {
            if (to == null || to.isEmpty()) {
                throw new CustomException("user.email.notnull");
            } else if (!Pattern.matches(EMAIL_REGEX, to)) {
                throw new CustomException("user.email.invalid");
            } else {
                helper.setTo(to);
                helper.setSubject(subject);
                String html = mailDesigns.sendTemporaryPasswordDesign(password,role);
                helper.setText(html, true);
                mailSender.send(mimeMessage);
            }
        } catch (MessagingException e) {
            throw new CustomException("email.send.error");
        }
    }
}
