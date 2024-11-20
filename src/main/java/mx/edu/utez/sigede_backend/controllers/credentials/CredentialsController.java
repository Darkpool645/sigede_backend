package mx.edu.utez.sigede_backend.controllers.credentials;


import mx.edu.utez.sigede_backend.controllers.credentials.DTO.GetCredentialsDTO;
import mx.edu.utez.sigede_backend.controllers.credentials.DTO.RequestCredentialDTO;
import mx.edu.utez.sigede_backend.services.credentials.CredentialService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
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
}