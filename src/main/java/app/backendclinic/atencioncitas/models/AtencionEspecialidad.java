package app.backendclinic.atencioncitas.models;

import app.backendclinic.service_medic.models.EspecialidadMedica;
import app.backendclinic.service_medic.models.HistorialMedico;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "atenciones_especialidades")
public class AtencionEspecialidad {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "cita_id", nullable = false)
    private Cita cita;

    @ManyToOne
    @JoinColumn(name = "especialidad_id", nullable = false)
    private EspecialidadMedica especialidad;

    @ManyToOne
    @JoinColumn(name = "historial_medico_id", nullable = false)
    private HistorialMedico historialMedico;

    @Column(name = "datos_formulario", columnDefinition = "TEXT", nullable = false)
    private String datosFormulario; // JSON con los datos del formulario

    @Column(name = "fecha_atencion", nullable = false)
    private LocalDateTime fechaAtencion;

    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
        fechaAtencion = LocalDateTime.now();
    }
}
