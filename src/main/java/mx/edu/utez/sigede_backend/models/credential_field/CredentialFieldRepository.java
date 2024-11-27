package mx.edu.utez.sigede_backend.models.credential_field;

import java.util.List;
import java.util.Optional;

import mx.edu.utez.sigede_backend.models.credential.Credential;
import mx.edu.utez.sigede_backend.models.user_info.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialFieldRepository extends JpaRepository<CredentialField, Long> {
    @Query("SELECT cf FROM CredentialField cf " +
            "JOIN FETCH cf.fkCredential c " +
            "WHERE c.credentialId = :credentialId")
    List<CredentialField> findByCredentialId(@Param("credentialId") Long credentialId);

    Optional<CredentialField> findByFkCredentialAndFkUserInfo(Credential credential, UserInfo userInfo);
}
