package app.backendclinic.service_medic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    //AL CREAR UN USUARIO SE CREE EL MEDICO, Y SIMPLEMENTE CUANDO
    //SE ASIGNE HORARIOS A ESE MEDICO YA CREADO 
    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private User usuario;
    
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore// Ignora la serialización de los roles para evitar la recursión infinita
    private List<HorarioAtencion> horariosAtencion;

    // Otros posibles atributos de Medico
    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }
}