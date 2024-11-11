package app.backendclinic.service_medic.dtos;

import java.time.LocalDate;

public class CreateAntecedenteRequest {
    private String descripcion;
    private LocalDate fechaRegistro;

    // Getters y Setters
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
