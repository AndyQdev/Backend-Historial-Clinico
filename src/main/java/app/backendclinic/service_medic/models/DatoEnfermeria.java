package app.backendclinic.service_medic.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "datos_enfermeria")
public class DatoEnfermeria {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "historial_medico_id", nullable = false)
    @JsonBackReference
    private HistorialMedico historialMedico;

    @Column(nullable = false)
    private double peso;

    @Column(nullable = false)
    private double presion;

    @Column(nullable = false)
    private double temperatura;

    @Column(nullable = false)
    private double saturacion;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }
}