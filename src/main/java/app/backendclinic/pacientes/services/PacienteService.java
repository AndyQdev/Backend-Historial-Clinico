package app.backendclinic.pacientes.services;

import org.springframework.stereotype.Service;

import app.backendclinic.pacientes.models.Paciente;
import app.backendclinic.pacientes.repositorys.PacienteRepository;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public Paciente findByEmail(String email) {
        return pacienteRepository.findByEmail(email).orElse(null);
    }
}
