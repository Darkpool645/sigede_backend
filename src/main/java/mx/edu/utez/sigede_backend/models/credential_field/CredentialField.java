package mx.edu.utez.sigede_backend.models.credential_field;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.credential.Credential;
import mx.edu.utez.sigede_backend.models.user_info.UserInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credential_fields")
public class CredentialField {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "credential_field_id", nullable = false)
    private Long credentialFieldId;

    @Column(name = "value", columnDefinition = "VARCHAR(255)", nullable = false)
    private String value;

    @ManyToOne
    @JoinColumn(name = "fk_credential", referencedColumnName = "credential_id", nullable = false)
    private  Credential fkCredential;

    @ManyToOne
    @JoinColumn(name = "fk_user_info", referencedColumnName = "user_info_id", nullable = false)
    private UserInfo fkUserInfo;

}
