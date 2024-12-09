package mx.edu.utez.sigede_backend.models.rol;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long>{
    Rol findByNameIgnoreCase(String name);
}
