package mx.edu.utez.sigede_backend.controllers.credentials;


import mx.edu.utez.sigede_backend.controllers.credentials.DTO.*;
import mx.edu.utez.sigede_backend.services.credentials.CredentialService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import mx.edu.utez.sigede_backend.controllers.credentials.DTO.*;
import mx.edu.utez.sigede_backend.models.credential.Credential;

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

    @PostMapping("/get-all-by-institution")
    public CustomResponse<Page<GetCretentialsByInstitutoIdDTO>> getAllByInstitution(@Validated @RequestBody RequestByInstitution payload, Pageable pageable){
        Page<GetCretentialsByInstitutoIdDTO> data = credentialService.getAllAccountByInstitution(payload,pageable);
        return new CustomResponse<>(200,"Usuarios",false,data);
    }


    
    @PostMapping("/get-credentials-by-name-and-capturist")
    public CustomResponse<Page<ResponseGetCredentialByNameAndCapturistDTO>> getCredentialsByNameAndCapturist(
            @RequestBody RequestGetCredentialsByNameAndCapturistDTO request) {
        Page<Credential> pages = credentialService.getCredentialsByNameAndCapturist(request.getName(), request.getCapturistId(),
                request.getPage(), request.getSize());
        Page<ResponseGetCredentialByNameAndCapturistDTO> response = pages.map(credential -> {
           ResponseGetCredentialByNameAndCapturistDTO responseDTO = new ResponseGetCredentialByNameAndCapturistDTO();
           responseDTO.setCredentialName(credential.getFullname());
           responseDTO.setPhoto(credential.getUserPhoto());
           return responseDTO;
        });

        return new CustomResponse<>(200, "Credenciales filtradas correctamente.", false, response);
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