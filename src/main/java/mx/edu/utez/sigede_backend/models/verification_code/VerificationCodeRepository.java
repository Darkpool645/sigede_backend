package mx.edu.utez.sigede_backend.models.verification_code;

import java.util.UUID;

import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID>{
    VerificationCode findByFkUserAccount(UserAccount fkUserAccount);
    void deleteByFkUserAccount(UserAccount fkUserAccount);

}
