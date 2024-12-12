package app.backendclinic.service_medic.Dtos;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicamentoRecetadoDto {
    private String anotacionHistorialId; // ID de la anotación
    private String nombre; // Nombre del medicamento
    private int dosis; // Dosis
    private String unidadDosis; // Unidad de dosis (mg, ml, etc.)
    private int frecuenciaHoras; // Cada cuántas horas
    private int duracionDias; // Por cuántos días
    private LocalDateTime horaInicio; // Hora de inicio (opcional)
}
