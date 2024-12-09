package mx.edu.utez.sigede_backend.services.doc_credential;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.xml.bind.JAXBElement;
import mx.edu.utez.sigede_backend.controllers.doc_credential.dto.ResponseDocCredentialDTO;
import mx.edu.utez.sigede_backend.models.credential.Credential;
import mx.edu.utez.sigede_backend.models.credential.CredentialRepository;
import mx.edu.utez.sigede_backend.models.credential_field.CredentialField;
import mx.edu.utez.sigede_backend.models.credential_field.CredentialFieldRepository;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.institution.InstitutionRepository;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.docx4j.dml.wordprocessingDrawing.Inline;
import org.docx4j.model.datastorage.migration.VariablePrepare;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.BinaryPartAbstractImage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Drawing;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
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
            replacements.put("name", credential.getFullname());
            replacements.put("expirationDate", credential.getExpirationDate().toLocalDate().toString());
            credentialFields.forEach(credentialField -> {
                if (credentialField.getFkUserInfo().isInCard()) {
                    String tag = credentialField.getFkUserInfo().getTag();
                    String field = credentialField.getValue();
                    replacements.put(tag, field);
                }
            });

            MainDocumentPart documentPart = wordMLPackage.getMainDocumentPart();

            VariablePrepare.prepare(wordMLPackage);
            documentPart.variableReplace(replacements);

            byte[] qrImageBytes = generateQR("https://www.youtube.com/");

            String imageUrl = credential.getUserPhoto();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                byte[] imageBytes = downloadImage(imageUrl);
                replacePlaceholderImage(wordMLPackage, imageBytes, qrImageBytes);
            }

            String outputPath = institution.getName() + "_" + credential.getFullname() + ".docx";
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            wordMLPackage.save(outputStream);
            wordMLPackage.save(new File("output.docx"));

            ResponseDocCredentialDTO dto = new ResponseDocCredentialDTO();
            dto.setOutputPath(outputPath);
            dto.setOutputStream(outputStream);

            return dto;
        } catch (Exception e) {
            throw new RuntimeException("Error procesing doc: " + e.getMessage());
        }
    }

    private byte[] downloadImage(String imageUrl) throws IOException {
        try (InputStream inputStream = new URL(imageUrl).openStream()) {
            return inputStream.readAllBytes();
        }
    }

    private void replacePlaceholderImage(WordprocessingMLPackage wordMLPackage, byte[] photoBytes, byte[] qrBytes) {
        try {
            List<Object> drawings = wordMLPackage.getMainDocumentPart().getJAXBNodesViaXPath("//w:drawing", true);
            Map<String, byte[]> map = new HashMap<>();
            map.put("PERSON_IMAGE", photoBytes);
            map.put("QR_CODE", qrBytes);

            map.forEach((altText, bytes) -> {
                for (Object obj : drawings) {
                    if (obj instanceof JAXBElement ) {
                        Object value = ((JAXBElement<?>) obj).getValue();
                        if (value instanceof Drawing drawing) {
                            for (Object element : drawing.getAnchorOrInline()) {
                                if (element instanceof Inline inline && inline.getDocPr() != null) {
                                    String currentAltText = inline.getDocPr().getDescr();
                                    if (currentAltText != null && currentAltText.equals(altText)) {
                                        try {
                                            BinaryPartAbstractImage imagePart = BinaryPartAbstractImage.createImagePart(
                                                    wordMLPackage, bytes);
                                            Inline originalInline = (Inline) drawing.getAnchorOrInline().get(0);
                                            Inline newInline = imagePart.createImageInline(
                                                    originalInline.getDocPr().getName(),
                                                    originalInline.getDocPr().getDescr(),
                                                    originalInline.getDocPr().getId(),
                                                    (int) originalInline.getGraphic().getGraphicData().getPic().getNvPicPr().getCNvPr().getId(),
                                                    false
                                            );
                                            newInline.setExtent(originalInline.getExtent());
                                            drawing.getAnchorOrInline().clear();
                                            drawing.getAnchorOrInline().add(newInline);
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            });
        } catch (Exception e){
            throw new RuntimeException("Error replacing image: " + e.getMessage());
        }
    }

    private byte[] generateQR(String content) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, 1000, 1000);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating QR code: " + e.getMessage());
        }
    }
}
