package mx.edu.utez.sigede_backend.models.institution;

import java.util.List;

import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.ResponseInstitutionsDTO;
import org.apache.coyote.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long>{

    Institution findByInstitutionId(Long id);

    boolean existsByName(String name);
}
