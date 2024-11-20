package mx.edu.utez.sigede_backend.models.credential_field;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialFieldRepository extends JpaRepository<CredentialField, Long> {
    List<CredentialField> findByFkCredential_CredentialId(Long credentialId);
}
