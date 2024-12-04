package mx.edu.utez.sigede_backend.controllers.doc_credential;

import lombok.extern.slf4j.Slf4j;
import mx.edu.utez.sigede_backend.controllers.doc_credential.dto.RequestDocCredentialDTO;
import mx.edu.utez.sigede_backend.controllers.doc_credential.dto.ResponseDocCredentialDTO;
import mx.edu.utez.sigede_backend.services.doc_credential.DocCredentialService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/download-credential")
public class DocCredentialController {
    private final DocCredentialService docCredentialService;

    public DocCredentialController(DocCredentialService docCredentialService) {
        this.docCredentialService = docCredentialService;
    }

    @PostMapping("/")
    public ResponseEntity<byte[]> downloadCredential(@RequestBody RequestDocCredentialDTO reqDocCredentialDTO) {
        ResponseDocCredentialDTO dto = docCredentialService.generateCredential(reqDocCredentialDTO.getInstitutionId(),
                reqDocCredentialDTO.getCredentialId());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + dto.getOutputPath() + "\"");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        return new ResponseEntity<>(dto.getOutputStream().toByteArray(), headers, HttpStatus.OK);
    }
}
