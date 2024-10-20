package mx.edu.utez.sigede_backend.models.user_account;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID>{
    
}
