package mx.edu.utez.sigede_backend.models.institution;
import java.util.UUID;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, UUID>{
    @Query("SELECT i FROM Institution i")
    List<Institution> getAllInstitutions();

    @Query("SELECT i FROM Institution i WHERE i.institutionId = :id")
    Institution getById(@Param("id") UUID id);

    boolean existsByName(String name);
}
