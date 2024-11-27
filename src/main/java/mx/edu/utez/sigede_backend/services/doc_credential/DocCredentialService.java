package mx.edu.utez.sigede_backend.services.doc_credential;

import mx.edu.utez.sigede_backend.models.credential_field.CredentialField;
import mx.edu.utez.sigede_backend.models.credential_field.CredentialFieldRepository;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;
import org.docx4j.model.fields.merge.DataFieldName;
import org.docx4j.model.fields.merge.MailMerger;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocCredentialService {
    private final InstitutionRepository institutionRepository;
    private final CredentialFieldRepository credentialFieldRepository;

    public DocCredentialService(InstitutionRepository institutionRepository, CredentialFieldRepository credentialFieldRepository) {
        this.institutionRepository = institutionRepository;
        this.credentialFieldRepository = credentialFieldRepository;
    }

    @Transactional
    public void generateCredential(Long institutionId, Long credentialId){
        if (institutionId == null || credentialId == null) {
            throw new IllegalArgumentException("The field must not be null");
        }
        Institution institution = institutionRepository.findByInstitutionId(institutionId);
        if (institution == null) {
            throw new IllegalArgumentException("Institution not found");
        }
        Blob doc = institution.getDocs();
        if (doc == null) {
            throw new IllegalArgumentException("Doc not found");
        }
        List<CredentialField> credentialFields = credentialFieldRepository.findByCredentialId(credentialId);
        if (credentialFields == null) {
            throw new IllegalArgumentException("Credential not found");
        }
        try {
            InputStream docStream = doc.getBinaryStream();

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(docStream);
            MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

            Map<DataFieldName, String> replacements = new HashMap<>();
            credentialFields.forEach(credentialField -> {
                if (credentialField.getFkUserInfo().isInCard()) {
                    String tag = "${" + credentialField.getFkUserInfo().getTag() + "}";
                    String field = credentialField.getValue();
                    replacements.put(new DataFieldName(tag), field);
                }
            });

            MailMerger.performMerge(wordMLPackage, replacements,true);

            String outputPath = "output.docx";
            wordMLPackage.save(new File(outputPath));
        } catch (Exception e) {
            throw new RuntimeException("Error processing doc", e);
        }
    }
}
