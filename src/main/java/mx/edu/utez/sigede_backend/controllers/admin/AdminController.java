package mx.edu.utez.sigede_backend.controllers.admin;

import mx.edu.utez.sigede_backend.controllers.admin.dto.RequestNewAdminDTO;
import mx.edu.utez.sigede_backend.services.admin.AdminService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public CustomResponse<String> registerAdmin(@Validated @RequestBody RequestNewAdminDTO payload){
        service.registerAdmin(payload);
        return new CustomResponse<>(201,"Admin Registrado correctamente",false,null);
    }
}
