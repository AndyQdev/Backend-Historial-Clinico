package app.backendclinic.service_medic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @ManyToMany(mappedBy = "especialidades")
    @JsonIgnore
    private List<Medico> medicos;

    // Relación muchos a muchos con ServicioMedico
    @ManyToMany
    @JoinTable(
        name = "servicio_especialidad",
        joinColumns = @JoinColumn(name = "especialidad_id"),
        inverseJoinColumns = @JoinColumn(name = "servicio_id")
    )
    @JsonIgnore // Evita la serialización infinita en la respuesta JSON
    @ToString.Exclude 
    private List<ServiceMedico> servicios;
    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }
}
