package app.backendclinic.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    String username;
    String nombre;
    String password;
    String apellido;
    // String direccion;
    String telefono;
    String roleId;
    // String branchId;
    boolean is_active;
}
