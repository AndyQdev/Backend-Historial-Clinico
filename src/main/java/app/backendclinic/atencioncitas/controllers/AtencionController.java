package app.backendclinic.atencioncitas.controllers;

import app.backendclinic.atencioncitas.dtos.RegistrarAtencionDTO;
import app.backendclinic.atencioncitas.models.AtencionEspecialidad;
import app.backendclinic.atencioncitas.services.AtencionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/atenciones")
public class AtencionController {

    @Autowired
    private AtencionService atencionService;

    @PostMapping
    public ResponseEntity<AtencionEspecialidad> registrarAtencion(@RequestBody RegistrarAtencionDTO dto) {
        AtencionEspecialidad atencion = atencionService.registrarAtencion(dto);
        return new ResponseEntity<>(atencion, HttpStatus.CREATED);
    }
}
