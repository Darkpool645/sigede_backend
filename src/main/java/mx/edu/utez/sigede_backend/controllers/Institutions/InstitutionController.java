package mx.edu.utez.sigede_backend.controllers.Institutions;

import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.PostInstitutionDTO;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.services.institution.InstitutionService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;

    public InstitutionController(InstitutionService institutionService) {
        this.institutionService = institutionService;
    }

    @GetMapping
    public ResponseEntity<List<Institution>> getAllInstitutions() {
        List<Institution> institutions = institutionService.getAllInstitutions();
        return new ResponseEntity<>(institutions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Institution> getInstitutionById(@PathVariable("id") UUID id) {
        Optional<Institution> institution = institutionService.getById(id);
        return institution.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/post-institution")
    public CustomResponse<Institution> postInstitution(@Validated @RequestBody PostInstitutionDTO payload) {
        Institution institution =institutionService.postInstitution(payload);
        return new CustomResponse<>(HttpStatus.CREATED.value(), "Institution creada correctamente.",
                false, institution);
    }
}
