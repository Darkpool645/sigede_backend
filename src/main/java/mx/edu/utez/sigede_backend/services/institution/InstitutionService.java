package mx.edu.utez.sigede_backend.services.institution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InstitutionService {

    @Autowired
    private InstitutionRepository institutionRepository;

    public List<Institution> getAllInstitutions() {
        return institutionRepository.getAllInstitutions();
    }

    public Optional<Institution> getById(UUID id) {
        return institutionRepository.findById(id);
    }
}