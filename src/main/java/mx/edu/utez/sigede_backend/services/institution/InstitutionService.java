package mx.edu.utez.sigede_backend.services.institution;

import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.*;
import mx.edu.utez.sigede_backend.models.institution.InstitutionStatus;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Blob;
import java.util.List;
import java.util.Optional;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;

    public InstitutionService(InstitutionRepository institutionRepository) {
        this.institutionRepository = institutionRepository;
    }

    @Transactional
    public ResponseInstitutionInfoDTO getById(Long id) {
        return institutionRepository.findInstitutionByInstitutionId(id);
    }

    public List<InstitutionDTO> getAllInstitutions() {
        return institutionRepository.getAllInstitutions();
  /*    
        return institutionRepository.findById(id)
                .map(institution -> new ResponseInstitutionInfoDTO(
                        institution.getInstitutionId(),
                        institution.getName(),
                        institution.getAddress(),
                        institution.getEmailContact(),
                        institution.getPhoneContact(),
                        institution.getLogo(),
                        institution.getInstitutionStatus()
                ))
                .orElseThrow(() -> new CustomException("institution.notfound"));
      */
    }

  
    @Transactional
    public List<ResponseBasicInstitutionDTO> getAllInstitutions() {
        return institutionRepository.findAll().stream().map(entity -> new ResponseBasicInstitutionDTO(
                entity.getInstitutionId(), entity.getName(), entity.getEmailContact(), entity.getLogo())).toList();
    }

    @Transactional
    public Page<Institution> getInstitutionsByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        return institutionRepository.findByNameContainingIgnoreCase(name, pageable);
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
        newInstitution.setInstitutionStatus(InstitutionStatus.HABILITADO);
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

    @Transactional
    public Institution updateInstitutionWithEmail(UpdateDTO payload) {
    //public Institution updateInstitutionWithEmail(RequestUpdateInstitutionDTO payload) {
        Optional<Institution> optionalInstitution = institutionRepository.findById(payload.getInstitutionId());

        if (optionalInstitution.isEmpty()) {
            throw new CustomException("institution.not.found.error");
        }

        Institution institution = optionalInstitution.get();

        if (payload.getInstitutionName() != null) {
            institution.setName(payload.getInstitutionName());
        }
        if (payload.getInstitutionAddress() != null) {
            institution.setAddress(payload.getInstitutionAddress());
        }
        if (payload.getInstitutionPhone() != null) {
            institution.setPhoneContact(payload.getInstitutionPhone());
        }
        if (payload.getInstitutionStatus() != null) {
            institution.setInstitutionStatus(payload.getInstitutionStatus());
        }
        if (payload.getLogo() != null) {
            institution.setLogo(payload.getLogo());
        }
        if (payload.getInstitutionEmail() != null) {
            institution.setEmailContact(payload.getInstitutionEmail());
        }

        return institutionRepository.save(institution);
    }

    @Transactional
    public InstitutionDocDTO getDocs(Long institutionId) {
        Optional<Institution> institutionOptional = institutionRepository.findById(institutionId);
        if (institutionOptional.isEmpty()) {
            throw new CustomException("institution.notfound");
        }

        Institution institution = institutionOptional.get();
        if (institution.getDocs() == null) {
            throw new CustomException("institution.docs.notfound");
        }

        try {
            Blob documentBlob = institution.getDocs();
            byte[] documentBytes = documentBlob.getBytes(1, (int) documentBlob.length());

            InstitutionDocDTO institutionDocsDTO = new InstitutionDocDTO();
            institutionDocsDTO.setInstitutionId(institution.getInstitutionId());
            institutionDocsDTO.setSuccess(true);
            institutionDocsDTO.setMessage("Documento encontrado.");
            institutionDocsDTO.setDocument(documentBytes);

            return institutionDocsDTO;

        } catch (Exception e) {
            throw new CustomException("institution.docs.error");
        }
    }

    @Transactional
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