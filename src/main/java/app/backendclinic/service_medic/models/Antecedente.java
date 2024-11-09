package app.backendclinic.service_medic.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "antecedentes")
public class Antecedente {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "historial_medico_id", nullable = false)
    @JsonBackReference
    private HistorialMedico historialMedico;

    @Column(nullable = false)
    private String descripcion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }
}
