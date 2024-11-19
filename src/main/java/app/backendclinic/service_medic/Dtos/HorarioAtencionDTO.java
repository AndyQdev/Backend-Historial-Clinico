package app.backendclinic.service_medic.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorarioAtencionDTO {
    private String id;
    private String dia;
    private String horaInicio;
    private String horaFin;
    private String doctor;
    private String service;
    private String specialty;
    private Double rating;
    private String descripcionServicio; // Nueva propiedad para la descripción del servicio
    private Double precioServicio;      // Nueva propiedad para el precio del servicio
    private String descripcionEspecialidad; 
    private String medicoId;
    private String specialtyId;
    private String serviceId;
}
