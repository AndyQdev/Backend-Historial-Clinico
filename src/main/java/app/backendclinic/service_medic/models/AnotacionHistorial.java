package app.backendclinic.service_medic.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "anotaciones_historial")
public class AnotacionHistorial {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "historial_medico_id", nullable = false)
    @JsonBackReference
    private HistorialMedico historialMedico;

    @ManyToOne
    @JoinColumn(name = "medico_id", nullable = false)
    private Medico medico;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;// Descripción general de la consulta

    @Column(nullable = true, columnDefinition = "TEXT")
    private String tratamiento;// Detalles del tratamiento recomendado

    @Column(name = "fecha_anotacion", nullable = false)
    private LocalDateTime fechaAnotacion;

    // Relación con Medicamentos Recetados
    @OneToMany(mappedBy = "anotacionHistorial", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<MedicamentoRecetado> medicamentosRecetados;

    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }
}
