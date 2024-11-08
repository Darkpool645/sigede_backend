package mx.edu.utez.sigede_backend.models.capturist_profile;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;


import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "capturist_profiles")
public class CapturistProfile {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "capturist_profile_id", nullable = false)
    private Long capturistProfileId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_profile",  referencedColumnName = "user_account_id")
    private UserAccount fkProfile;


}

