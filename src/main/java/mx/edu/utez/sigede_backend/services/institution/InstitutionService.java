package mx.edu.utez.sigede_backend.services.institution;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.PostInstitutionDTO;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;

    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    public List<Institution> getAllInstitutions() {
        return institutionRepository.getAllInstitutions();
    }

    public Optional<Institution> getById(Long id) {
        return institutionRepository.findById(id);
    }

    @Transactional
    public Institution postInstitution(PostInstitutionDTO payload) {
        if (institutionRepository.existsByName(payload.getInstitutionName())) {
            throw new CustomException("institution.name.error");
        }
        Institution newInstitution = new Institution();
        newInstitution.setName(payload.getInstitutionName());
        newInstitution.setAddress(payload.getInstitutionAddress());
        newInstitution.setEmailContact(payload.getInstitutionEmail());
        newInstitution.setPhoneContact(payload.getInstitutionPhone());
        newInstitution.setLogo(payload.getLogo());
        institutionRepository.save(newInstitution);
        return newInstitution;
    }
}