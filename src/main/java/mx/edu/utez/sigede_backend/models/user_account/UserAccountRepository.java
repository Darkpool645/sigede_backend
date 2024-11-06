package mx.edu.utez.sigede_backend.models.user_account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID>{

    //@Query("select usac from UserAccount as usac where usac.email = :email")
    //Optional<UserAccount> getOneByEmail(@Param("email") String encryptedEmail);
    Optional<UserAccount> findFirstByEmail(String email);
    @Query("SELECT ua FROM UserAccount ua")
    List<UserAccount> getAllUserAccounts();

    @Query("SELECT usac FROM UserAccount usac WHERE usac.fkRol.name = :roleName")
    List<UserAccount> getAllAdmins(@Param("roleName") String roleName);

    @Query("SELECT usac FROM UserAccount usac WHERE usac.userAccountId = :id")
    UserAccount getById(@Param("id") UUID id);

    @Query("SELECT ua FROM UserAccount ua WHERE ua.fkInstitution.institutionId = :institutionId AND ua.fkRol.name = 'Admin'")
    List<UserAccount> findAdministratorsByInstitution(@Param("institutionId") UUID institutionId);

    boolean existsByUserAccountId(UUID userAccountId);
    UserAccount findByUserAccountId(UUID userAccountId);
    boolean existsByEmail(String email);
    UserAccount findByEmail(String email);
}
