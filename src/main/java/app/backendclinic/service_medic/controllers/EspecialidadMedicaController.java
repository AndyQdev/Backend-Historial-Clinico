package app.backendclinic.service_medic.controllers;

import app.backendclinic.models.User;
import app.backendclinic.service_medic.dtos.EspecialidadMedicaDto;
import app.backendclinic.service_medic.models.EspecialidadMedica;

import app.backendclinic.service_medic.services.EspecialidadMedicaService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/especialidades")
public class EspecialidadMedicaController {

    @Autowired
    private EspecialidadMedicaService service;

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    // Crear una nueva especialidad médica
    @PostMapping("/crear")
    public ResponseEntity<EspecialidadMedica> crearEspecialidad(@RequestBody EspecialidadMedicaDto dto, HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();
        return new ResponseEntity<>(service.crearEspecialidad(dto, usuario, ipAddress), HttpStatus.CREATED);
    }

    // Actualizar una especialidad médica existente por su ID
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<EspecialidadMedica> actualizarEspecialidad(
            @PathVariable String id, @RequestBody EspecialidadMedicaDto dto, HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();
        return new ResponseEntity<>(service.actualizarEspecialidad(id, dto, usuario, ipAddress), HttpStatus.OK);
    }

    // Eliminar una especialidad médica por su ID
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> eliminarEspecialidad(@PathVariable String id, HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();
        service.eliminarEspecialidad(id, usuario, ipAddress);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Obtener una lista de especialidades médicas con paginación
    @GetMapping("/listar")
    public ResponseEntity<Page<EspecialidadMedica>> listarEspecialidades(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();
        return new ResponseEntity<>(service.listarEspecialidades(PageRequest.of(page, size), usuario, ipAddress), HttpStatus.OK);
    }

    // Obtener una especialidad médica específica por su ID
    @GetMapping("/detalle/{id}")
    public ResponseEntity<EspecialidadMedica> obtenerEspecialidadPorId(@PathVariable String id, HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();
        return new ResponseEntity<>(service.obtenerEspecialidadPorId(id, usuario, ipAddress), HttpStatus.OK);
    }
}
