package mx.edu.utez.sigede_backend.models.capturist_profile;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CapturistProfileRepository extends JpaRepository<CapturistProfile, UUID>{
    
}
