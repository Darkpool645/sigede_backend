package mx.edu.utez.sigede_backend.models.user_account;

import java.util.List;

import jakarta.transaction.Transactional;
import mx.edu.utez.sigede_backend.controllers.user_accounts.dto.ChangePasswordDataConsult;
import mx.edu.utez.sigede_backend.controllers.user_accounts.dto.GetUserBasicInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import mx.edu.utez.sigede_backend.controllers.capturers.dto.GetCapturistsDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long>{

    @Query("SELECT new mx.edu.utez.sigede_backend.controllers.capturers.dto.GetCapturistsDTO(u.userAccountId, u.name, u.fkStatus.name) " +
            "FROM UserAccount u " +
            "WHERE u.fkRol.name = 'capturista' AND u.fkInstitution.institutionId = :institutionId")
    List<GetCapturistsDTO> findCapturistasByInstitution(@Param("institutionId") Long institutionId);

    @Query("SELECT ua FROM UserAccount ua")
    List<UserAccount> getAllUserAccounts();

    @Query("SELECT  new mx.edu.utez.sigede_backend.controllers.user_accounts.dto.GetUserBasicInfoDTO(usac.userAccountId, usac.name, usac.fkStatus.name)" +
            "FROM UserAccount usac WHERE usac.fkRol.name = :roleName")
    List<GetUserBasicInfoDTO> getAllByRolName(@Param("roleName") String roleName);

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

    @Query("SELECT ua FROM UserAccount ua WHERE ua.userAccountId=:userAccountId AND ua.fkInstitution.institutionId=:institutionId")
    UserAccount findByUserAccountIdAndFkInstitution(@Param("userAccountId")Long userAccountId,@Param("institutionId")Long institutionId);


    boolean existsByUserAccountId(Long userAccountId);


    UserAccount findByUserAccountId(Long userAccountId);

    boolean existsByEmail(String email);

    UserAccount findByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END " +
            "FROM UserAccount u " +
            "WHERE u.email = :email AND u.userAccountId <> :userAccountId")
    boolean existsByEmailAndNotUserAccountId(@Param("email") String email, @Param("userAccountId") Long userAccountId);

    @Query("SELECT new mx.edu.utez.sigede_backend.controllers.user_accounts.dto.ChangePasswordDataConsult(uc.password,uc.fkInstitution.logo)  from UserAccount uc where uc.email=:email")
    ChangePasswordDataConsult getOldPasswordByEmail(@Param("email") String email);

    @Modifying
    @Transactional
    @Query("UPDATE UserAccount ua SET ua.password = :password WHERE ua.email = :email")
    void updatePasswordByEmail(@Param("email") String email, @Param("password") String password);
}
