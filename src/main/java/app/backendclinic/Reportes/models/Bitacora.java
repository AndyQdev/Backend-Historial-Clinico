package app.backendclinic.Reportes.models;


import app.backendclinic.models.User;
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
@Table(name = "bitacora")
public class Bitacora {

    @Id
    private String id;

    @Column(name = "accion", nullable = false)
    private String accion;

    @Column(name = "fechahora", nullable = false)
    private LocalDateTime fechaHora;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonBackReference
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(name = "tipoaccion", nullable = false)
    private String tipoAccion;

    @Column(name = "ipaddress", nullable = false)
    private String ipAddress;

    @Column(name = "resultado", nullable = false)
    private String resultado;

    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }
}

