package mx.edu.utez.sigede_backend.controllers.Institutions;

import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.InstitutionStatusUpdateDTO;
import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.InstitutionUpdateDTO;
import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.PostInstitutionDTO;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.services.institution.InstitutionService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;
    private final ErrorDictionary errorDictionary;

    public InstitutionController(InstitutionService institutionService, ErrorDictionary errorDictionary) {
        this.institutionService = institutionService;
        this.errorDictionary = errorDictionary;
    }

    @GetMapping
    public ResponseEntity<List<Institution>> getAllInstitutions() {
        List<Institution> institutions = institutionService.getAllInstitutions();
        return new ResponseEntity<>(institutions, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Institution> getInstitutionById(@PathVariable("id") Long id) {
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

    @PutMapping("/update-institution")
    public ResponseEntity<CustomResponse<Institution>> updateInstitution(
            @Validated @RequestBody InstitutionUpdateDTO payload) {
        try {
            Institution updatedInstitution = institutionService.updateInstitution(payload);
            CustomResponse<Institution> response = new CustomResponse<>(
                    HttpStatus.OK.value(), "Institution actualizada correctamente.", false, updatedInstitution);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            String errorMessage = errorDictionary.getErrorMessage(e.getErrorCode());

            CustomResponse<Institution> response = new CustomResponse<>(
                    HttpStatus.BAD_REQUEST.value(), errorMessage, true, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            CustomResponse<Institution> response = new CustomResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocurrió un error inesperado.", true, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update-institution-status")
    public ResponseEntity<CustomResponse<Institution>> updateInstitutionStatus(
            @RequestBody InstitutionStatusUpdateDTO payload) {
        try {
            Institution updatedInstitution = institutionService.updateInstitutionStatus(
                    payload.getInstitutionId(), payload.getInstitutionStatus());
            CustomResponse<Institution> response = new CustomResponse<>(
                    HttpStatus.OK.value(), "Status de la institución actualizado correctamente.", false, updatedInstitution);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            String errorMessage = errorDictionary.getErrorMessage(e.getErrorCode());

            CustomResponse<Institution> response = new CustomResponse<>(
                    HttpStatus.BAD_REQUEST.value(), errorMessage, true, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            CustomResponse<Institution> response = new CustomResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocurrió un error inesperado.", true, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
