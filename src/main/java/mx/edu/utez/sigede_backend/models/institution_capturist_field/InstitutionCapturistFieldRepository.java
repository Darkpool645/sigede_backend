package mx.edu.utez.sigede_backend.models.institution_capturist_field;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitutionCapturistFieldRepository extends JpaRepository<InstitutionCapturistField, Long> {
    List<InstitutionCapturistField> findAllByFkInstitution_InstitutionId(Long institutionId);
}
