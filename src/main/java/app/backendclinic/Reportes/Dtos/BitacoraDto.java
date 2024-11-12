package app.backendclinic.Reportes.Dtos;


import java.time.LocalDateTime;

public class BitacoraDto {
    private String accion;
    private LocalDateTime fechaHora;
    private String tipoAccion;
    private String ipAddress;
    private String resultado;
    private String usuarioNombre;
    private String usuarioRol;

    // Constructor
    public BitacoraDto(String accion, LocalDateTime fechaHora, String tipoAccion, String ipAddress, String resultado, String usuarioNombre, String usuarioRol) {
        this.accion = accion;
        this.fechaHora = fechaHora;
        this.tipoAccion = tipoAccion;
        this.ipAddress = ipAddress;
        this.resultado = resultado;
        this.usuarioNombre = usuarioNombre;
        this.usuarioRol = usuarioRol;
    }

    // Getters y Setters
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getTipoAccion() { return tipoAccion; }
    public void setTipoAccion(String tipoAccion) { this.tipoAccion = tipoAccion; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getUsuarioRol() { return usuarioRol; }
    public void setUsuarioRol(String usuarioRol) { this.usuarioRol = usuarioRol; }
}

