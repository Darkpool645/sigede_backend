package mx.edu.utez.sigede_backend.models.credential;

import java.util.List;
import java.util.UUID;

import mx.edu.utez.sigede_backend.controllers.credentials.DTO.GetCredentialsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CredentialRepository extends JpaRepository<Credential, Long>{
    @Query("SELECT new mx.edu.utez.sigede_backend.controllers.credentials.DTO.GetCredentialsDTO(c.credentialId, c.fullname, c.userPhoto) " +
            "FROM Credential c WHERE c.fkUserAccount.userAccountId = :userAccountId")
    List<GetCredentialsDTO> findCredentialsByUserAccountId(Long userAccountId);
}
