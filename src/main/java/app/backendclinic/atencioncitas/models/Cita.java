package app.backendclinic.atencioncitas.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import app.backendclinic.pacientes.models.Paciente;
import app.backendclinic.service_medic.models.EspecialidadMedica;
import app.backendclinic.service_medic.models.Medico;
import app.backendclinic.service_medic.models.ServiceMedico;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "citas")
public class Cita {
    
    @Id
    private String id;

    @Column(name = "codigo_cita", nullable = false, unique = true)
    private String codigoCita;

    @Column(name = "fecha_creada", nullable = false)
    private LocalDateTime fechaCreada;

    @ManyToOne
    @JoinColumn(name = "especialidad_id", nullable = false)
    private EspecialidadMedica especialidad;

    // Relación con Medico (Muchos a Uno)
    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    // Relación con Paciente (Muchos a Uno)
    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    // Relación con ServicioMedico (Muchos a Uno)
    @ManyToOne
    @JoinColumn(name = "servicio_medico_id", nullable = false)
    private ServiceMedico servicioMedico;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoCita estado = EstadoCita.PENDIENTE;

    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
        if (codigoCita == null || codigoCita.isEmpty()) {
            codigoCita = generateCodigoCita();
        }
        fechaCreada = LocalDateTime.now();
    }

    private String generateCodigoCita() {
        return "CITA-" + (int)(Math.random() * 1000000);
    }
}
