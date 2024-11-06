package mx.edu.utez.sigede_backend.models.institution;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.util.UUID;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "institutions")
public class Institution {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "institution_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID institutionId;

    @Column(name = "name", columnDefinition = "VARCHAR(255)", nullable = false)
    private String name;

    @Column(name = "address", columnDefinition = "VARCHAR(255)", nullable = false)
    private String address;

    @Column(name = "phone_contact", columnDefinition = "VARCHAR(30)", nullable = false)
    private String phoneContact;

    @Column(name = "email_contact", columnDefinition = "VARCHAR(255)", nullable = false)
    private String emailContact;


    @Column(name = "logo", nullable = false, columnDefinition = "TEXT")
    @Size(max = 500, message = "El logo no puede tener más de 500 caracteres")
    private String logo;

    @PrePersist
    private void generateUUID() {
        if (this.institutionId == null) {
            this.institutionId = UUID.randomUUID();
        }
    }
}
