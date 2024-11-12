package app.backendclinic.user.controllers;

import java.util.List;

import app.backendclinic.Reportes.services.BitacoraService;
import app.backendclinic.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import app.backendclinic.user.models.Permiso;
import app.backendclinic.user.repositorys.PermisoRepository;



@RestController
@RequestMapping("/api/permiso")
public class PermisoController {
        @Autowired
    private PermisoRepository permisoRepository;

    @Autowired
    private BitacoraService bitacoraService;

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    @GetMapping
    public List<Permiso> getAllPermisos(HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();

        try {
            List<Permiso> permisos = permisoRepository.findAll();
            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA TODOS LOS PERMISOS", ipAddress, "Exitoso");
            return permisos;
        } catch (Exception e) {
            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA TODOS LOS PERMISOS", ipAddress, "Fallido");
            throw e;
        }
    }

    @GetMapping("/{id}")
    public Permiso getPermisoById(@PathVariable String id, HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();

        try {
            Permiso permiso = permisoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Permiso no encontrado"));
            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA PERMISO POR ID", ipAddress, "Exitoso");
            return permiso;
        } catch (Exception e) {
            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA PERMISO POR ID", ipAddress, "Fallido");
            throw e;
        }
    }
    @PostMapping
    public Permiso createPermiso(@RequestBody Permiso permiso){
        return permisoRepository.save(permiso);
    }
}
