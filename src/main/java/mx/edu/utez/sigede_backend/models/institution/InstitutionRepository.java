package mx.edu.utez.sigede_backend.models.institution;
import java.util.List;

import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.InstitutionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long>{
    @Query("SELECT new mx.edu.utez.sigede_backend.controllers.Institutions.DTO.InstitutionDTO(i.institutionId, i.name, i.logo) FROM Institution i")
    List<InstitutionDTO> getAllInstitutions();

    @Query("SELECT i FROM Institution i WHERE i.institutionId = :id")
    Institution getById(@Param("id") Long id);

    Institution findByInstitutionId(Long id);

    boolean existsByName(String name);
}
