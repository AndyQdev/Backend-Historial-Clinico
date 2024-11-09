package app.backendclinic.service_medic.Dtos;

import java.time.LocalDateTime;

public class CreateDatoEnfermeriaRequest {

    private double peso;
    private double presion;
    private double temperatura;
    private double saturacion;
    private LocalDateTime fechaRegistro;

    // Getters y Setters
    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getPresion() {
        return presion;
    }

    public void setPresion(double presion) {
        this.presion = presion;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getSaturacion() {
        return saturacion;
    }

    public void setSaturacion(double saturacion) {
        this.saturacion = saturacion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
