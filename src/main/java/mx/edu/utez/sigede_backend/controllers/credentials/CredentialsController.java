package mx.edu.utez.sigede_backend.controllers.credentials;

import mx.edu.utez.sigede_backend.controllers.credentials.DTO.*;
import mx.edu.utez.sigede_backend.models.credential.Credential;
import mx.edu.utez.sigede_backend.services.credentials.CredentialService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
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

    public CredentialsController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping("/capturist/{userAccountId}")
    public ResponseEntity<List<GetCredentialsDTO>> getCredentialsByCapturerId(@PathVariable Long userAccountId) {
        List<GetCredentialsDTO> credentials = credentialService.getCredentialsByCapturerId(userAccountId);
        return new ResponseEntity<>(credentials, HttpStatus.OK);
    }

    @PostMapping("/get-all-by-institution")
    public CustomResponse<Page<GetCredentialsDTO>> getAllByInstitution(@Validated @RequestBody RequestByInstitutionDTO request,
                                                                       Pageable pageable) {
        Page<Credential> credentials = credentialService.getAllCredentialsByInstitution(request.getInstitutionId(), pageable);

        Page<GetCredentialsDTO> response = credentials.map(credential -> {
            GetCredentialsDTO dto = new GetCredentialsDTO();
            dto.setCredentialId(credential.getCredentialId());
            dto.setExpirationDate(credential.getExpirationDate());
            dto.setFullname(credential.getFullname());
            dto.setUserPhoto(credential.getUserPhoto());
            return dto;
        });

        return new CustomResponse<>(200, "Credenciales encontradas", false, response);
    }

    @PostMapping("/get-credentials-by-name-and-capturist")
    public CustomResponse<Page<GetCredentialsDTO>> getCredentialsByNameAndCapturist(
            @RequestBody RequestGetCredentialsByNameAndCapturistDTO request) {
        Page<Credential> pages = credentialService.getCredentialsByNameAndCapturist(request.getName(), request.getCapturistId(),
                request.getPage(), request.getSize());
        Page<GetCredentialsDTO> response = pages.map(credential -> {
            GetCredentialsDTO responseDTO = new GetCredentialsDTO();
            responseDTO.setCredentialId(credential.getCredentialId());
            responseDTO.setFullname(credential.getFullname());
            responseDTO.setUserPhoto(credential.getUserPhoto());
            responseDTO.setExpirationDate(credential.getExpirationDate());
        return responseDTO;
        });

        return new CustomResponse<>(200, "Credenciales filtradas correctamente.", false, response);
    }

    @PostMapping("/new-credential")
    public CustomResponse<String> postNewCredential(@Validated @RequestBody RequestCredentialDTO payload){
        credentialService.createCredential(payload);
        return new CustomResponse<>(201,"Credencial registrada correctamente",false, null);
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
        return new CustomResponse<>(201,"Informacion actualizada",false,null);
    }
}