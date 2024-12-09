package mx.edu.utez.sigede_backend.models.credential;

import java.util.List;

import mx.edu.utez.sigede_backend.controllers.credentials.DTO.GetCredentialsDTO;
import mx.edu.utez.sigede_backend.controllers.credentials.DTO.GetCretentialsByInstitutoIdDTO;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long>{
    @Query("SELECT new mx.edu.utez.sigede_backend.controllers.credentials.DTO.GetCredentialsDTO(c.credentialId, c.fullname, c.userPhoto, c.expirationDate) " +
            "FROM Credential c WHERE c.fkUserAccount.userAccountId = :userAccountId")
    List<GetCredentialsDTO> findCredentialsByUserAccountId(Long userAccountId);

    @Query("""
       SELECT new mx.edu.utez.sigede_backend.controllers.credentials.DTO.GetCretentialsByInstitutoIdDTO(
           c.credentialId,
           c.fullname,
           c.userPhoto,
           c.expirationDate
       )
       FROM Credential c 
       WHERE c.fkInstitution.institutionId = :institutionId
         AND (:fullname IS NULL OR :fullname = '' OR c.fullname LIKE %:fullname%)
       """)
    Page<GetCretentialsByInstitutoIdDTO> findAllByFkInstitutionAndFullname(
            @Param("institutionId") Long institutionId,
            @Param("fullname") String fullname,
            Pageable pageable);


    Page<Credential> findAllByFkInstitution_InstitutionIdAndFullnameContainingIgnoreCase(Long institutionId, String name, Pageable pageable);

    Credential findCredentialByCredentialId(Long credentialId);

    Page<Credential> findByFullnameContainingIgnoreCaseAndAndFkUserAccount(String fullname, UserAccount userAccount, Pageable pageable);
}
