package mx.edu.utez.sigede_backend.models.user_account;

import java.util.List;

import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.rol.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mx.edu.utez.sigede_backend.controllers.capturers.dto.GetCapturistsDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long>{

    @Query("SELECT new mx.edu.utez.sigede_backend.controllers.capturers.dto.GetCapturistsDTO(u.userAccountId, u.name, u.fkStatus.name) " +
            "FROM UserAccount u " +
            "WHERE u.fkRol.name = 'capturista' AND u.fkInstitution.institutionId = :institutionId")
    List<GetCapturistsDTO> findCapturistasByInstitution(@Param("institutionId") Long institutionId);

    Page<UserAccount> findByNameContainingIgnoreCaseAndFkInstitutionAndFkRol(String name, Institution institution, Rol rol, Pageable page);

    Page<UserAccount> findByNameContainingIgnoreCaseAndFkRol(String name, Rol rol, Pageable page);

    @Query("SELECT ua FROM UserAccount ua")
    List<UserAccount> getAllUserAccounts();

    @Query("SELECT usac FROM UserAccount usac WHERE usac.fkRol.name = :roleName")
    List<UserAccount> getAllAdmins(@Param("roleName") String roleName);

    @Query("SELECT ua FROM UserAccount ua WHERE ua.fkInstitution.institutionId = :institutionId AND ua.fkRol.name = 'Admin'")
    List<UserAccount> findAdministratorsByInstitution(@Param("institutionId") Long institutionId);
    Page<UserAccount> findAllByFkRol_NameAndFkInstitution_InstitutionIdAndName(String role, Long institutionId, String name, Pageable pageable);

    UserAccount findByUserAccountIdAndFkRol_NameAndFkInstitution_InstitutionId(Long userId ,String role, Long institutionId);

    UserAccount findByUserAccountId(Long userAccountId);
    boolean existsByEmail(String email);
    UserAccount findByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END " +
            "FROM UserAccount u " +
            "WHERE u.email = :email AND u.userAccountId <> :userAccountId")
    boolean existsByEmailAndNotUserAccountId(@Param("email") String email, @Param("userAccountId") Long userAccountId);

    List<UserAccount>  findAllByFkRol_NameAndFkInstitution_InstitutionId(String role, Long institutionId);
}
