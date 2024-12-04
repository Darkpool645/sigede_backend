package mx.edu.utez.sigede_backend.models.capturist_profile_field;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapturistProfileFieldRepository extends JpaRepository<CapturistProfileField, Long>{
    
}
