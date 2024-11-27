package mx.edu.utez.sigede_backend.models.user_account;

import java.util.List;

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

    @Query("SELECT ua FROM UserAccount ua")
    List<UserAccount> getAllUserAccounts();

    @Query("SELECT  new mx.edu.utez.sigede_backend.controllers.capturers.dto.GetCapturistsDTO(usac.userAccountId, usac.name, usac.fkStatus.name)" +
            "FROM UserAccount usac WHERE usac.fkRol.name = :roleName")
    List<GetCapturistsDTO> getAllAdmins(@Param("roleName") String roleName);

    @Query("SELECT ua FROM UserAccount ua WHERE ua.fkInstitution.institutionId = :institutionId AND ua.fkRol.name = 'Admin'")
    List<UserAccount> findAdministratorsByInstitution(@Param("institutionId") Long institutionId);

    @Query("""
       SELECT u FROM UserAccount u WHERE u.fkRol.name = :role
         AND u.fkInstitution.institutionId = :institutionId
         AND (:name IS NULL OR :name = '' OR u.name LIKE %:name%)
       """)
    Page<UserAccount> findAllByFkRol_NameAndFkInstitution_InstitutionIdAndName(
            @Param("role") String role,
            @Param("institutionId") Long institutionId,
            @Param("name") String name,
            Pageable pageable);
    
    boolean existsByUserAccountId(Long userAccountId);

    UserAccount findByUserAccountId(Long userAccountId);

    boolean existsByEmail(String email);

    UserAccount findByEmail(String email);
}
