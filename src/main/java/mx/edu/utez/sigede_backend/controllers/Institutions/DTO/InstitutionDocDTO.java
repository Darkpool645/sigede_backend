package mx.edu.utez.sigede_backend.controllers.Institutions.DTO;

import lombok.Data;

import java.sql.Blob;

@Data
public class InstitutionDocDTO {
    private Long institutionId;
    private boolean success;
    private String message;
}
