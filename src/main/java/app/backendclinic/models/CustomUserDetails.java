package app.backendclinic.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final Paciente paciente;

    public CustomUserDetails(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Podrías definir roles o permisos si lo necesitas
        return Collections.emptyList(); // Clientes no tienen roles por defecto
    }

    @Override
    public String getPassword() {
        return paciente.getPassword(); // Retornar la contraseña del cliente si es necesaria
    }

    @Override
    public String getUsername() {
        return paciente.getEmail(); // Retornar el email como nombre de usuario
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
        return paciente.isActive(); // Si el cliente tiene un campo activo
    }

    public Paciente getCliente() {
        return paciente;
    }
}

