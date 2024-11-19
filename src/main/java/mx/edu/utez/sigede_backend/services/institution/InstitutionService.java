package mx.edu.utez.sigede_backend.services.institution;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.InstitutionDocDTO;
import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.InstitutionUpdateDTO;
import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.PostInstitutionDTO;
import mx.edu.utez.sigede_backend.models.institution.InstitutionStatus;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;
import java.sql.Blob;
import java.util.List;
import java.util.Optional;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;

    @Autowired
    private ErrorDictionary errorDictionary;

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

    @Transactional
    public Institution updateInstitution(InstitutionUpdateDTO payload) {
        Optional<Institution> optionalInstitution = institutionRepository.findById(payload.getInstitutionId());

        if (optionalInstitution.isEmpty()) {
            throw new CustomException("institution.not.found.error");
        }

        Institution institution = optionalInstitution.get();

        if (payload.getName() != null) {
            institution.setName(payload.getName());
        }
        if (payload.getAddress() != null) {
            institution.setAddress(payload.getAddress());
        }
        if (payload.getPhoneContact() != null) {
            institution.setPhoneContact(payload.getPhoneContact());
        }
        if (payload.getInstitutionStatus() != null) {
            institution.setInstitutionStatus(payload.getInstitutionStatus());
        }
        if (payload.getLogo() != null) {
            institution.setLogo(payload.getLogo());
        }

        return institutionRepository.save(institution);
    }

    public InstitutionDocDTO getDocs(Long institutionId) {
        Optional<Institution> institutionOptional = institutionRepository.findById(institutionId);
        if (institutionOptional.isEmpty()) {
            throw new CustomException("institution.notfound");
        }

        Institution institution = institutionOptional.get();
        if (institution.getDocs() == null) {
            throw new CustomException("institution.docs.notfound");
        }

        InstitutionDocDTO institutionDocsDTO = new InstitutionDocDTO();
        institutionDocsDTO.setInstitutionId(institution.getInstitutionId());
        institutionDocsDTO.setSuccess(true);
        institutionDocsDTO.setMessage("Documento encontrado.");
        return institutionDocsDTO;
    }

    public InstitutionDocDTO createOrUpdateDocs(Long institutionId, Blob docs) {
        if (docs == null) {
            throw new CustomException("field.not.null");
        }

        Optional<Institution> institutionOptional = institutionRepository.findById(institutionId);
        if (institutionOptional.isEmpty()) {
            throw new CustomException("institution.notfound");
        }

        Institution institution = institutionOptional.get();
        institution.setDocs(docs);
        institutionRepository.save(institution);

        InstitutionDocDTO institutionDocsDTO = new InstitutionDocDTO();
        institutionDocsDTO.setInstitutionId(institution.getInstitutionId());
        institutionDocsDTO.setSuccess(true);
        institutionDocsDTO.setMessage("Documento actualizado con éxito.");
        return institutionDocsDTO;
    }

    @Transactional
    public Institution updateInstitutionStatus(Long institutionId, String institutionStatus) {
        Optional<Institution> optionalInstitution = institutionRepository.findById(institutionId);

        if (optionalInstitution.isEmpty()) {
            throw new CustomException("institution.not.found.error");
        }

        Institution institution = optionalInstitution.get();

        try {
            institution.setInstitutionStatus(InstitutionStatus.valueOf(institutionStatus));
        } catch (IllegalArgumentException e) {
            throw new CustomException("invalid.institution.status");
        }

        return institutionRepository.save(institution);
    }

}