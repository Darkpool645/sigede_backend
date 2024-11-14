package mx.edu.utez.sigede_backend.models.user_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.*;

import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_info")
public class UserInfo {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_info_id", nullable = false)
    private Long userInfoId;

    @Column(name = "tag", columnDefinition = "VARCHAR(255)", nullable = false)
    private String tag;

    @Column(name = "type", columnDefinition = "VARCHAR(100)", nullable = false)
    private String type;

    @Column(name = "is_in_qr", columnDefinition = "BOOLEAN", nullable = false)
    private boolean isInQr;

    @Column(name = "is_in_card", columnDefinition = "BOOLEAN", nullable = false)
    private boolean isInCard;

}
