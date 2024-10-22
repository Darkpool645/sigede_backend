package mx.edu.utez.sigede_backend.models.user_account;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID>{

    @Query("select usac from UserAccount as usac where usac.email = :email")
    Optional<UserAccount> getOneByEmail(@Param("email") String encryptedEmail);

}
