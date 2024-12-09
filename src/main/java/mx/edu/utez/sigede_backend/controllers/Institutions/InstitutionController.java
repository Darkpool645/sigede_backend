package mx.edu.utez.sigede_backend.controllers.Institutions;

import mx.edu.utez.sigede_backend.controllers.Institutions.DTO.*;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
import org.springframework.data.domain.Page;
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

@RestController
@RequestMapping("/api/institutions")
public class InstitutionController {

    private final InstitutionService institutionService;
    private final ErrorDictionary errorDictionary;

    public InstitutionController(InstitutionService institutionService, ErrorDictionary errorDictionary) {
        this.institutionService = institutionService;
        this.errorDictionary = errorDictionary;
    }

    @GetMapping("/get-all")
    public CustomResponse<List<InstitutionDTO>> getAllInstitutions(){
        List<InstitutionDTO> data = institutionService.getAllInstitutions();

        return new CustomResponse<>(200,"Todas las instituciones",false,data);
    }

    @GetMapping("/{id}")
    public CustomResponse<ResponseInstitutionInfoDTO> getInstitutionByid(@PathVariable Long id){
        ResponseInstitutionInfoDTO institution = institutionService.getById(id);
        return new CustomResponse<>(200,"institution",false,institution);
    }

    @PostMapping("/get-institutions-by-name")
    public CustomResponse<Page<ResponseBasicInstitutionDTO>> getInstitutionsByName(@Validated @RequestBody
                                                                                   RequestGetInstitutionsByNameDTO request) {
        Page<Institution> pages = institutionService.getInstitutionsByName(request.getName(), request.getPage(), request.getSize());
        Page<ResponseBasicInstitutionDTO> response = pages.map(institution -> {
            ResponseBasicInstitutionDTO institutionDTO = new ResponseBasicInstitutionDTO();
            institutionDTO.setInstitutionId(institution.getInstitutionId());
            institutionDTO.setName(institution.getName());
            institutionDTO.setLogo(institution.getLogo());
            institutionDTO.setEmail_contact(institution.getEmailContact());
            return institutionDTO;
        });
        return new CustomResponse<>(200, "Instituciones filtradas correctamente.", false, response);
    }

    @PostMapping("/post-institution")
    public CustomResponse<ResponseInstitutionsDTO> postInstitution(@Validated @RequestBody PostInstitutionDTO payload) {
        Institution institution = institutionService.postInstitution(payload);
        ResponseInstitutionsDTO institutionDTO = new ResponseInstitutionsDTO();
        institutionDTO.setId(institution.getInstitutionId());
        institutionDTO.setName(institution.getName());
        institutionDTO.setEmail_contact(institution.getEmailContact());
        institutionDTO.setLogo(institution.getLogo());
        return new CustomResponse<>(HttpStatus.CREATED.value(), "Institution creada correctamente.",
                false, institutionDTO);
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
    @PutMapping("/update")
    public ResponseEntity<CustomResponse<ResponseInstitutionUpdateDTO>> updateInstitutionWitEmailContact(
            @Validated @RequestBody UpdateDTO payload) {
        try {
            Institution updatedInstitution = institutionService.updateInstitutionWithEmail(payload);
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

    @PutMapping("/update")
    public ResponseEntity<CustomResponse<ResponseInstitutionUpdateDTO>> updateInstitutionWitEmailContact(
            @Validated @RequestBody RequestUpdateInstitutionDTO payload) {
        try {
            Institution updatedInstitution = institutionService.updateInstitutionWithEmail(payload);
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
