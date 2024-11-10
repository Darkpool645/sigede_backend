package mx.edu.utez.sigede_backend.models.user_account;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mx.edu.utez.sigede_backend.controllers.capturers.dto.GetCapturistsDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long>{

    @Query("SELECT new mx.edu.utez.sigede_backend.controllers.capturers.dto.GetCapturistsDTO(u.userAccountId, u.name, u.fkStatus.name) " +
            "FROM UserAccount u " +
            "WHERE u.fkRol.name = 'capturista' AND u.fkInstitution.institutionId = :institutionId")
    List<GetCapturistsDTO> findCapturistasByInstitution(@Param("institutionId") Long institutionId);

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
    List<UserAccount> findAdministratorsByInstitution(@Param("institutionId") Long institutionId);
    Page<UserAccount> findAllByFkRol_NameAndFkInstitution_InstitutionId(String role, Long institutionId, Pageable pageable);
    boolean existsByUserAccountId(Long userAccountId);
    UserAccount findByUserAccountId(Long userAccountId);
    boolean existsByEmail(String email);
    UserAccount findByEmail(String email);
}
