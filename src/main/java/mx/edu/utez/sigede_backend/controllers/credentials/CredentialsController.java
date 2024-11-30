package mx.edu.utez.sigede_backend.controllers.credentials;

import mx.edu.utez.sigede_backend.controllers.credentials.DTO.GetCredentialsDTO;
import mx.edu.utez.sigede_backend.controllers.credentials.DTO.RequestCredentialDTO;
import mx.edu.utez.sigede_backend.controllers.credentials.DTO.RequestUpdateCredentialDTO;
import mx.edu.utez.sigede_backend.controllers.credentials.DTO.ResponseCredentialDTO;
import mx.edu.utez.sigede_backend.services.credentials.CredentialService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/credentials")
public class CredentialsController {

    private final CredentialService credentialService;

    public CredentialsController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping("/capturist/{userAccountId}")
    public ResponseEntity<List<GetCredentialsDTO>> getCredentialsByCapturerId(@PathVariable Long userAccountId) {
        List<GetCredentialsDTO> credentials = credentialService.getCredentialsByCapturerId(userAccountId);
        return new ResponseEntity<>(credentials, HttpStatus.OK);
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