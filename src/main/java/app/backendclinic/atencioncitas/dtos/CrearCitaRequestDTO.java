package app.backendclinic.atencioncitas.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearCitaRequestDTO {
    private String medicoId;
    private String pacienteId;
    private String servicioId;
    private String codigoCita; // si quieres que el cliente lo envíe o si lo quieres generar en el servicio, puedes omitirlo aquí
    private String especialidad;
}