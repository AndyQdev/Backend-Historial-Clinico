package app.backendclinic.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pacientes", uniqueConstraints = {@UniqueConstraint(columnNames = {"numeroSeguro"})})
public class Paciente {

    @Id
    private String id; // Se generará automáticamente un UUID como id

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false)
    private LocalDate fechaNacimiento;

    @Column(nullable = false, unique = true)
    private String numeroSeguro;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "code_expiration")
    private LocalDateTime codeExpiration;

    @PrePersist
    public void prePersist() {
        if (id == null || id.isEmpty()) {
            id = UUID.randomUUID().toString();
        }
    }
}