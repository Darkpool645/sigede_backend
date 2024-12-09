package mx.edu.utez.sigede_backend.models.institution;

import java.util.List;

import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.InstitutionDTO;
import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.ResponseInstitutionInfoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long>{
    @Query("SELECT new mx.edu.utez.sigede_backend.controllers.Institutions.DTO.InstitutionDTO(i.institutionId,i.name,i.logo) FROM Institution i")
    List<InstitutionDTO> getAllInstitutions();


    Institution findByInstitutionId(Long id);

    Page<Institution> findByNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByName(String name);

    @Query("SELECT new mx.edu.utez.sigede_backend.controllers.Institutions.DTO.ResponseInstitutionInfoDTO(ins.institutionId,ins.name,ins.address,ins.emailContact,ins.phoneContact,ins.logo,ins.institutionStatus)  FROM Institution ins where  ins.institutionId=:institutionId")
    ResponseInstitutionInfoDTO findInstitutionByInstitutionId(@Param("institutionId") Long institutionId);
}
