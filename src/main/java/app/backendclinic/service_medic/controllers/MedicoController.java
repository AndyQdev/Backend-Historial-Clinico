package app.backendclinic.service_medic.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import app.backendclinic.service_medic.models.HorarioAtencion;
import app.backendclinic.service_medic.models.Medico;
import app.backendclinic.service_medic.services.MedicoService;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public List<Medico> getAllMedicos() {
        return medicoService.getAllMedicos();
    }

    @GetMapping("/{id}")
    public Medico getMedicoById(@PathVariable String id) {
        return medicoService.getMedicoById(id);
    }

    @PostMapping
    public Medico createMedico(@RequestBody Medico medico) {
        return medicoService.createMedico(medico);
    }

    @PostMapping("/{id}/horarios")
    public HorarioAtencion addHorarioToMedico(@PathVariable String id, @RequestBody HorarioAtencion horario) {
        return medicoService.addHorarioToMedico(id, horario);
    }

    @GetMapping("/{id}/horarios")
    public List<HorarioAtencion> getHorariosByMedicoId(@PathVariable String id) {
        return medicoService.getHorariosByMedicoId(id);
    }
}