package app.backendclinic.service_medic.models;

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
@Table(name = "especialidades_medicas")
public class EspecialidadMedica {

    @Id
    private String id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre; // Nombre de la especialidad

    @Column(length = 255)
    private String descripcion; // Descripción de la especialidad

    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }
}
