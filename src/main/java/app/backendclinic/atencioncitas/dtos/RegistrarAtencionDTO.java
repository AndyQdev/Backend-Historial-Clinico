package app.backendclinic.atencioncitas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrarAtencionDTO {
    private String citaId; // ID de la cita asociada
    private String especialidadId; // Especialidad seleccionada
    private Map<String, Object> datosFormulario; // Campos dinámicos del formulario
}
