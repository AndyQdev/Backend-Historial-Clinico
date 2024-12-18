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

import app.backendclinic.models.User;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medicos")
public class Medico {
    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    @ToString.Exclude // Evita que se incluya en toString
    private User usuario;

    @Column(nullable = true)
    private Double rating;
    
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude // Evita ciclos con HorarioAtencion
    private List<HorarioAtencion> horariosAtencion;

    // Relación muchos a muchos con EspecialidadMedica
    @ManyToMany
    @JoinTable(
        name = "medico_especialidad",
        joinColumns = @JoinColumn(name = "medico_id"),
        inverseJoinColumns = @JoinColumn(name = "especialidad_id")
    )
    @ToString.Exclude // Evita ciclos con HorarioAtencion
    private List<EspecialidadMedica> especialidades;

    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }
}
