package app.backendclinic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})})
public class Usuario implements UserDetails {

    @Id
    private String id; // Se generará automáticamente un UUID como id

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String username; // Nombre de usuario único

    @Column(nullable = false)
    private String password; // Contraseña encriptada

    @Column(nullable = false)
    private String telefono; // Teléfono del usuario

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role; // Relación con el rol

    @Column(name = "is_active", nullable = false)
    private boolean isActive; // Campo para saber si el usuario está activo o no

    // PrePersist para generar el UUID automáticamente
    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }

    // Implementación de UserDetails para Spring Security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getName())); // Retorna el rol como autoridad
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Modificar si necesitas gestionar expiración de cuenta
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Modificar si necesitas bloquear cuentas
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Modificar si necesitas gestionar expiración de credenciales
    }

    @Override
    public boolean isEnabled() {
        return isActive; // Determina si la cuenta está habilitada en base a `isActive`
    }
}