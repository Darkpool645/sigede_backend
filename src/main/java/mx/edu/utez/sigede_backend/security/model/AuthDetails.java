package mx.edu.utez.sigede_backend.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthDetails implements UserDetails {
    private String email;
    private String password;
    private String status;
    private String role;
    private Long institutionId;

    private Long userId;

    public AuthDetails(UserAccount userAccount) {
        this.email = userAccount.getEmail();
        this.password = userAccount.getPassword();
        this.status = userAccount.getFkStatus().getName();
        this.role = userAccount.getFkRol().getName();
        this.institutionId = (userAccount.getFkInstitution() != null) ? userAccount.getFkInstitution().getInstitutionId() : null;

        this.userId = userAccount.getUserAccountId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getInstitutionsId(){
        return institutionId;
    }
}
