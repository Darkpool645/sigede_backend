package mx.edu.utez.sigede_backend.controllers.admin.dto;

import lombok.Data;

@Data
public class ResponseGetByNameDTO {
    private Long id;
    private String name;
    private String status;
}
