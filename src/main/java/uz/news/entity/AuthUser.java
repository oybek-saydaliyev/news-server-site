package uz.news.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import uz.news.base.BaseEntity;
import uz.news.entity.enums.AuthRole;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "users")
public class AuthUser extends BaseEntity implements UserDetails {
    private String username;
    private String email;
    private String fullName;
    private String password;
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    private AuthRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    public AuthUser(String username, String password, AuthRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
