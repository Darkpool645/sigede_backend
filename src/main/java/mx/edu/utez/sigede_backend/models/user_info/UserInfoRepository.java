package mx.edu.utez.sigede_backend.models.user_info;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long>{
    UserInfo findByTag(String tag);
}
