package app.backendclinic.service_medic.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.backendclinic.service_medic.dtos.HorarioAtencionDTO;
import app.backendclinic.service_medic.services.HorarioAtencionService;

@RestController
@RequestMapping("/api/horarios")
public class HorarioController {
    // @Autowired
    // private HorarioAtencionRepository horarioAtencionRepository;

    // @GetMapping
    // public List<HorarioAtencion> getAllHorarios(){
    //     return horarioAtencionRepository.findAll();
    // }
    @Autowired
    private HorarioAtencionService horarioAtencionService;
    
    @GetMapping
    public List<HorarioAtencionDTO> getAllHorarios() {
        return horarioAtencionService.getAllHorariosWithDetails();
    }
}
