package mx.edu.utez.sigede_backend.models.institution;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "institutions")
public class Institution {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "institution_id", nullable = false)
    private Long institutionId;

    @Column(name = "name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @Column(name = "address", columnDefinition = "VARCHAR(255)", nullable = false)
    private String address;

    @Column(name = "phone_contact", columnDefinition = "VARCHAR(30)", nullable = false)
    private String phoneContact;

    @Column(name = "email_contact", columnDefinition = "VARCHAR(255)", nullable = false)
    private String emailContact;

    @Enumerated(EnumType.STRING)
    @Column(name = "institution_status", columnDefinition = "ENUM('HABILITADO', 'INHABILITADO')", nullable = false)
    private InstitutionStatus institutionStatus;

    @Column(name = "logo", nullable = false, columnDefinition = "TEXT")
    @Size(max = 500, message = "El logo no puede tener más de 500 caracteres")
    private String logo;

    @Lob
    @Column(name = "docs", nullable = false)
    private Blob docs;
}
