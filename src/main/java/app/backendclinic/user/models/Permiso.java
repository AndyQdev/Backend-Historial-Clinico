package app.backendclinic.user.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "permissions")
public class Permiso {

    @Id
    private String id; // Usamos UUID como identificador

    @Column(nullable = false, unique = true)
    private String name; // Nombre del permiso, por ejemplo, "READ_PRODUCT", "WRITE_PRODUCT", etc.

    private String description; // Descripción del permiso

    // PrePersist para generar el UUID automáticamente
    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }

    @Override
    public String toString() {
        return "Permiso [id=" + id + ", name=" + name + ", description=" + description + "]";
    }
}
