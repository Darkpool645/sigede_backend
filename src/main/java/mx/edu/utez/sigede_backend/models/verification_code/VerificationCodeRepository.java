package mx.edu.utez.sigede_backend.models.verification_code;

import jakarta.transaction.Transactional;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    VerificationCode findByFkUserAccount(UserAccount fkUserAccount);
    void deleteByFkUserAccount(UserAccount fkUserAccount);

    @Transactional
    @Modifying
    @Query("DELETE FROM VerificationCode vc WHERE vc.fkUserAccount.email = :email")
    void deleteVerificationCodeByUserEmail(@Param("email") String email);

    @Query("SELECT vc FROM VerificationCode vc where vc.fkUserAccount.email=:email and  vc.verificationCode=:code")
    VerificationCode FindByUserEmailAndVerificationCode(@Param("email") String email,@Param("code") String code);
}
