package mx.edu.utez.sigede_backend.models.institution;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long>{

    Institution findByInstitutionId(Long id);

    Page<Institution> findByNameContainingIgnoreCase(String name, Pageable pageable);

    boolean existsByName(String name);
}
