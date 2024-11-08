package mx.edu.utez.sigede_backend.models.user_account;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.rol.Rol;
import mx.edu.utez.sigede_backend.models.status.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_accounts")
public class UserAccount {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_account_id", nullable = false)
    private Long userAccountId;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_rol", referencedColumnName = "rol_id", nullable = false, columnDefinition = "BINARY(16)")
    private Rol fkRol;

    @ManyToOne
    @JoinColumn(name = "fk_status", referencedColumnName = "status_id", nullable = false, columnDefinition = "BINARY(16)")
    private Status fkStatus;

    @ManyToOne
    @JoinColumn(name ="fk_institution", referencedColumnName = "institution_id", nullable = true, columnDefinition = "BINARY(16)")
    private Institution fkInstitution;

}
