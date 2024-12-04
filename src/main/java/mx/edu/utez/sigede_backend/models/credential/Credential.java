package mx.edu.utez.sigede_backend.models.credential;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credentials")
public class Credential {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credential_id", nullable = false)
    private Long credentialId;

    @Column(name = "issue_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime issueDate;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "fullname",nullable = false)
    private String fullname;

    @Lob
    @Column(name = "user_photo", nullable = false)
    private String userPhoto;

    @ManyToOne
    @JoinColumn(name = "fk_institution", referencedColumnName = "institution_id", nullable = false)
    private Institution fkInstitution;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_account", referencedColumnName = "user_account_id", nullable = false, unique = false)
    private UserAccount fkUserAccount;

}
