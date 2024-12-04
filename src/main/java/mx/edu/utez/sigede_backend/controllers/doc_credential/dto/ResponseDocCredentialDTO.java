package mx.edu.utez.sigede_backend.controllers.doc_credential.dto;

import lombok.Data;

import java.io.ByteArrayOutputStream;

@Data
public class ResponseDocCredentialDTO {
    private ByteArrayOutputStream outputStream;
    private String outputPath;
}
