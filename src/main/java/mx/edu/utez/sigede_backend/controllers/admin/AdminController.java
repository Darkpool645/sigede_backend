package mx.edu.utez.sigede_backend.controllers.admin;

import mx.edu.utez.sigede_backend.controllers.admin.dto.RequestGetByNameAndInstitutionDTO;
import mx.edu.utez.sigede_backend.controllers.admin.dto.RequestNewAdminDTO;
import mx.edu.utez.sigede_backend.controllers.admin.dto.ResponseGetByNameDTO;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.services.admin.AdminService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import org.springframework.data.domain.Page;
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

    @PostMapping("/get-admins-by-name-and-institution")
    public CustomResponse<Page<ResponseGetByNameDTO>> getAdminsByNameAndInstitution(@Validated @RequestBody
                                                                                        RequestGetByNameAndInstitutionDTO request) {
        Page<UserAccount> pages = service.getAdminsByNameAndInstitution(request.getName(), request.getInstitutionId(),
                request.getPage(), request.getSize());

        Page<ResponseGetByNameDTO> response = pages.map(userAccount -> {
            ResponseGetByNameDTO dto = new ResponseGetByNameDTO();
            dto.setId(userAccount.getUserAccountId());
            dto.setName(userAccount.getName());
            dto.setStatus(userAccount.getFkStatus().getName());
            return dto;
        });

        return new CustomResponse<>(200, "Administradores filtrados correctamente.", false, response);
    }

    @PostMapping("/register")
    public CustomResponse<String> registerAdmin(@Validated @RequestBody RequestNewAdminDTO payload){
        service.registerAdmin(payload);
        return new CustomResponse<>(201,"Admin Registrado correctamente",false,null);
    }
}
