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
@Table(name = "medicamentos_recetados")
public class MedicamentoRecetado {

    @Id
    private String id; // UUID como clave primaria

    @ManyToOne
    @JoinColumn(name = "anotacion_historial_id", nullable = false)
    @JsonBackReference
    private AnotacionHistorial anotacionHistorial;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String nombre; // Nombre del medicamento

    @Column(nullable = false)
    private int dosis; // Cantidad de medicamento

    @Column(nullable = false, columnDefinition = "TEXT")
    private String unidadDosis; // Unidad de la dosis (e.g., "mg", "unidades", "ml")

    @Column(nullable = false)
    private int frecuenciaHoras; // Cada cuántas horas debe tomarse

    @Column(nullable = false)
    private int duracionDias; // Por cuántos días debe tomarse

    @Column(nullable = true)
    private LocalDateTime horaInicio; // Hora en la que el paciente comienza a tomarlo

    @Column(nullable = false)
    private boolean completado; // Estado del tratamiento: completado o no

    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
        this.completado = false;
    }
    }


