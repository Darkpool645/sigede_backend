package mx.edu.utez.sigede_backend.controllers.user_accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDataConsult {
    String oldPassword;
    String logo;
}
