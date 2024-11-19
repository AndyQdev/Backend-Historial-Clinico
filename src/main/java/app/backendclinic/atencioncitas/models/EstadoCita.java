package app.backendclinic.atencioncitas.models;

public enum EstadoCita {
    PENDIENTE,      // Cita creada pero no confirmada
    CONFIRMADA,     // Cita confirmada por el sistema o el médico
    EN_CURSO,       // Cita en proceso de atención
    COMPLETADA,     // Cita finalizada exitosamente
    CANCELADA,      // Cita cancelada antes o durante la atención
    NO_ASISTIO,     // Paciente no se presentó
    REPROGRAMADA    // Cita movida a otro horario
}
