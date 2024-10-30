package app.backendclinic.services;

import org.springframework.stereotype.Service;

import app.backendclinic.models.Paciente;
import app.backendclinic.repositorys.PacienteRepository;

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
