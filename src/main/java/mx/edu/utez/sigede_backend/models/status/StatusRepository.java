package mx.edu.utez.sigede_backend.models.status;

import  java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {
    Status findByName(String name);

    Status findByStatusId(Long id);
}
