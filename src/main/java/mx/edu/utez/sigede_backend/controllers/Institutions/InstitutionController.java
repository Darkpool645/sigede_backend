package mx.edu.utez.sigede_backend.controllers.Institutions;

import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.*;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.services.institution.InstitutionService;
import org.springframework.web.multipart.MultipartFile;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
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
    public ResponseEntity<List<InstitutionDTO>> getAllInstitutions() {
        List<InstitutionDTO> institutions = institutionService.getAllInstitutions();
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
    public ResponseEntity<CustomResponse<ResponseInstitutionUpdateDTO>> updateInstitution(
            @Validated @RequestBody InstitutionUpdateDTO payload) {
        try {
            Institution updatedInstitution = institutionService.updateInstitution(payload);
            ResponseInstitutionUpdateDTO responseInstitutionUpdateDTO = getResponseInstitutionUpdateDTO(updatedInstitution);

            CustomResponse<ResponseInstitutionUpdateDTO> response = new CustomResponse<>(
                    HttpStatus.OK.value(), "Institution actualizada correctamente.", false, responseInstitutionUpdateDTO);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            String errorMessage = errorDictionary.getErrorMessage(e.getErrorCode());

            CustomResponse<ResponseInstitutionUpdateDTO> response = new CustomResponse<>(
                    HttpStatus.BAD_REQUEST.value(), errorMessage, true, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            CustomResponse<ResponseInstitutionUpdateDTO> response = new CustomResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(), "Ocurrió un error inesperado.", true, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private static ResponseInstitutionUpdateDTO getResponseInstitutionUpdateDTO(Institution updatedInstitution) {
        ResponseInstitutionUpdateDTO responseInstitutionUpdateDTO = new ResponseInstitutionUpdateDTO();
        responseInstitutionUpdateDTO.setInstitutionStatus(updatedInstitution.getInstitutionStatus());
        responseInstitutionUpdateDTO.setName(updatedInstitution.getName());
        responseInstitutionUpdateDTO.setLogo(updatedInstitution.getLogo());
        responseInstitutionUpdateDTO.setAddress(updatedInstitution.getAddress());
        responseInstitutionUpdateDTO.setPhoneContact(updatedInstitution.getPhoneContact());
        responseInstitutionUpdateDTO.setInstitutionId(updatedInstitution.getInstitutionId());
        return responseInstitutionUpdateDTO;
    }

    @PutMapping("/update-institution-status")
    public ResponseEntity<CustomResponse<ResponseInstitutionUpdateDTO>> updateInstitutionStatus(
            @RequestBody InstitutionStatusUpdateDTO payload) {
        try {
            Institution updatedInstitution = institutionService.updateInstitutionStatus(
                    payload.getInstitutionId(), payload.getInstitutionStatus());
            ResponseInstitutionUpdateDTO responseInstitutionUpdateDTO = getResponseInstitutionUpdateDTO(updatedInstitution);
            CustomResponse<ResponseInstitutionUpdateDTO> response = new CustomResponse<>(
                    HttpStatus.OK.value(),
                    "Status de la institución actualizado correctamente.",
                    false,
                    responseInstitutionUpdateDTO);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            String errorMessage = errorDictionary.getErrorMessage(e.getErrorCode());
            CustomResponse<ResponseInstitutionUpdateDTO> response = new CustomResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    errorMessage,
                    true,
                    null);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            CustomResponse<ResponseInstitutionUpdateDTO> response = new CustomResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Ocurrió un error inesperado.",
                    true,
                    null);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{institutionId}/docs")
    public ResponseEntity<?> getDocs(@PathVariable Long institutionId) {
        try {
            InstitutionDocDTO institutionDocsDTO = institutionService.getDocs(institutionId);
            return ResponseEntity.ok(institutionDocsDTO);
        } catch (CustomException e) {
            String errorMessage = errorDictionary.getErrorMessage(e.getErrorCode());
            return ResponseEntity.badRequest().body(errorMessage);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error desconocido.");
        }
    }


    @PutMapping("/{institutionId}/patch/docs")
    public ResponseEntity<?> createOrUpdateDocs(
            @PathVariable Long institutionId,
            @RequestPart("docs") MultipartFile docs) {
        try {
            Blob blob = new SerialBlob(docs.getBytes());
            InstitutionDocDTO institutionDocsDTO = institutionService.createOrUpdateDocs(institutionId, blob);

            return ResponseEntity.ok(institutionDocsDTO);
        } catch (CustomException e) {
            return ResponseEntity.badRequest().body(errorDictionary.getErrorMessage(e.getErrorCode()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error desconocido.");
        }
    }


}
