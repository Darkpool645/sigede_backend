package mx.edu.utez.sigede_backend.controllers.admin.dto;

import lombok.Data;

@Data
public class ResponseGetByNameDTO {
    private Long userId;
    private String name;
    private String email;
    private String status;
}
