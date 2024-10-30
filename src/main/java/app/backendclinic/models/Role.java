package app.backendclinic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    private String id; // Usamos UUID como identificador

    @Column(nullable = false, unique = true)
    private String name; // Nombre del rol, por ejemplo, "Administrador" o "Cliente"

    @OneToMany(mappedBy = "role")
    @JsonIgnore// Ignora la serialización de los roles para evitar la recursión infinita
    private List<Usuario> users; // Relación con los usuarios que tienen este rol

    @ManyToMany
    @JoinTable(
        name = "role_permissions",
        joinColumns = @JoinColumn(name = "role_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private List<Permiso> permissions; // Relación con la entidad Permiso

    // PrePersist para generar el UUID automáticamente
    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }
    @Override
    public String toString() {
        return "Role [id=" + id + ", name=" + name + "]";
    }
}