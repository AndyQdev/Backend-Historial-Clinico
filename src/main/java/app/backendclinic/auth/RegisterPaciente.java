package app.backendclinic.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPaciente {
    private String email;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String numeroSeguro; // Número de seguro único para cada paciente
    private String telefono;
    private String direccion;
    private String password;
    private boolean isActive;
}