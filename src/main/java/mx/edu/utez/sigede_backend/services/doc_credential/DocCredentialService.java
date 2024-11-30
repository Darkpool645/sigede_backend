package mx.edu.utez.sigede_backend.services.doc_credential;

import jakarta.xml.bind.JAXBElement;
import mx.edu.utez.sigede_backend.controllers.doc_credential.dto.ResponseDocCredentialDTO;
import mx.edu.utez.sigede_backend.models.credential.Credential;
import mx.edu.utez.sigede_backend.models.credential.CredentialRepository;
import mx.edu.utez.sigede_backend.models.credential_field.CredentialField;
import mx.edu.utez.sigede_backend.models.credential_field.CredentialFieldRepository;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.Part;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.wml.Text;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocCredentialService {
    private final InstitutionRepository institutionRepository;
    private final CredentialRepository credentialRepository;
    private final CredentialFieldRepository credentialFieldRepository;

    public DocCredentialService(InstitutionRepository institutionRepository, CredentialRepository credentialRepository,
                                CredentialFieldRepository credentialFieldRepository) {
        this.institutionRepository = institutionRepository;
        this.credentialRepository = credentialRepository;
        this.credentialFieldRepository = credentialFieldRepository;
    }

    @Transactional
    public ResponseDocCredentialDTO generateCredential(Long institutionId, Long credentialId){
        Institution institution = institutionRepository.findByInstitutionId(institutionId);
        if (institution == null) {
            throw new CustomException("institution.notfound");
        }
        Blob doc = institution.getDocs();
        if (doc == null) {
            throw new CustomException("institution.docs.notfound");
        }
        Credential credential = credentialRepository.findCredentialByCredentialId(credentialId);
        if (credential == null) {
            throw new CustomException("credential.not.found");
        }
        List<CredentialField> credentialFields = credentialFieldRepository.findByCredentialId(credentialId);
        try {
            InputStream docStream = doc.getBinaryStream();

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(docStream);

            Map<String, String> replacements = new HashMap<>();
            replacements.put("${name}", credential.getFullname());
            replacements.put("${expirationDate}", credential.getExpirationDate().toLocalDate().toString());
            credentialFields.forEach(credentialField -> {
                if (credentialField.getFkUserInfo().isInCard()) {
                    String tag = "${" + credentialField.getFkUserInfo().getTag() + "}";
                    String field = credentialField.getValue();
                    replacements.put(tag, field);
                }
            });
            replaceTextInDocument(wordMLPackage, replacements);

            String imageUrl = credential.getUserPhoto();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                byte[] imageBytes = downloadImage(imageUrl);
                replacePlaceholderImage(wordMLPackage, imageBytes);
            }

            String outputPath = institution.getName() + "_" + credential.getFullname() + ".docx";
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            wordMLPackage.save(outputStream);

            ResponseDocCredentialDTO dto = new ResponseDocCredentialDTO();
            dto.setOutputPath(outputPath);
            dto.setOutputStream(outputStream);

            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Error processing doc", e.getCause());
        }
    }

    private void replaceTextInDocument(WordprocessingMLPackage wordMLPackage, Map<String, String> replacements) throws Exception {
        List<Object> texts = wordMLPackage.getMainDocumentPart().getJAXBNodesViaXPath("//w:t", true);
        for (Object obj : texts) {
            if (obj instanceof JAXBElement) {
                Object value = ((JAXBElement<?>) obj).getValue();
                if (value instanceof Text element) {
                    String text = element.getValue();
                    for (Map.Entry<String, String> entry : replacements.entrySet()) {
                        if (text.contains(entry.getKey())) {
                            element.setValue(text.replace(entry.getKey(), entry.getValue()));
                        }
                    }
                }
            }
        }
    }

    private byte[] downloadImage(String imageUrl) throws IOException {
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            return inputStream.readAllBytes();
        }
    }

    private void replacePlaceholderImage(WordprocessingMLPackage wordMLPackage, byte[] newImageBytes) {
        try {
            BinaryPartAbstractImage imagePart = null;
            for (Part part : wordMLPackage.getParts().getParts().values()) {
                if (part instanceof BinaryPartAbstractImage singlePartImage) {
                    imagePart = singlePartImage;
                    break;
                }
            }

            if (imagePart == null) {
                throw new CustomException("image.not.found");
            }

            imagePart.setBinaryData(newImageBytes);
        } catch (Exception e){
            System.out.println("Error replacing image: " + e.getMessage());
        }
    }
}
