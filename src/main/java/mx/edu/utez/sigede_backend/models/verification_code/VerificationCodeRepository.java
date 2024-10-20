package mx.edu.utez.sigede_backend.models.verification_code;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, UUID>{
    
}
