package mx.edu.utez.sigede_backend.controllers.credentials;


import mx.edu.utez.sigede_backend.controllers.credentials.DTO.*;
import mx.edu.utez.sigede_backend.controllers.user_accounts.dto.RequestAllAdminByInstitutionDTO;
import mx.edu.utez.sigede_backend.controllers.user_accounts.dto.ResponseAllAdminByInstitutionDTO;
import mx.edu.utez.sigede_backend.services.credentials.CredentialService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/credentials")
public class CredentialsController {

    private final CredentialService credentialService;
    private final ErrorDictionary errorDictionary;

    public CredentialsController(CredentialService credentialService, ErrorDictionary errorDictionary) {
        this.credentialService = credentialService;
        this.errorDictionary = errorDictionary;
    }

    @GetMapping("/capturist/{userAccountId}")
    public ResponseEntity<List<GetCredentialsDTO>> getCredentialsByCapturerId(@PathVariable Long userAccountId) {
        List<GetCredentialsDTO> credentials = credentialService.getCredentialsByCapturerId(userAccountId);
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }
    @PostMapping("/get-all-by-institution")
    public CustomResponse<Page<GetCretentialsByInstitutoIdDTO>> getAllByInstitution(@Validated @RequestBody RequestByInstitution payload, Pageable pageable){
        Page<GetCretentialsByInstitutoIdDTO> data = credentialService.getAllAccountByInstitution(payload,pageable);
        return new CustomResponse<>(200,"Usuarios",false,data);
    }


    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException ex) {
        String errorMessage = errorDictionary.getErrorMessage(ex.getErrorCode());
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/new-credential")
    public CustomResponse<String> postNewCredential(@Validated @RequestBody RequestCredentialDTO payload){
        credentialService.createCredential(payload);
        return new CustomResponse<>(200,"Credencial registrada correctamente",false, null);
    }

    @GetMapping("/{credentialId}")
    public CustomResponse<ResponseCredentialDTO> getCredentialWithFields(@PathVariable Long credentialId) {
        ResponseCredentialDTO credentialDTO = credentialService.getCredentialWithFields(credentialId);
        return new CustomResponse<>(200,"Informacion obtenida",false,credentialDTO);
    }

    @PutMapping("/{credentialId}")
    public CustomResponse<String> updateCredential(@PathVariable Long credentialId,
                                                                  @RequestBody RequestUpdateCredentialDTO updateDTO) {
        credentialService.updateCredential(credentialId, updateDTO);
        return new CustomResponse<>(200,"Informacion actualizada",false,null);
    }
}