package app.backendclinic.service_medic.controllers;

import app.backendclinic.service_medic.models.ServiceMedico;
import app.backendclinic.service_medic.services.ServicioMedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios-medicos")
public class ServiceMedicoController {

    @Autowired
    private ServicioMedicoService servicioMedicoService;

    @PostMapping("/crear")
    public ResponseEntity<ServiceMedico> crearServicio(@RequestBody ServiceMedico servicioMedico) {
        return new ResponseEntity<>(servicioMedicoService.crearServicio(servicioMedico), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ServiceMedico>> listarServicios() {
        return new ResponseEntity<>(servicioMedicoService.listarServicios(), HttpStatus.OK);
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ServiceMedico> actualizarServicio(
            @PathVariable String id, @RequestBody ServiceMedico servicioMedico) {
        return new ResponseEntity<>(servicioMedicoService.actualizarServicio(id, servicioMedico), HttpStatus.OK);
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable String id) {
        servicioMedicoService.eliminarServicio(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
