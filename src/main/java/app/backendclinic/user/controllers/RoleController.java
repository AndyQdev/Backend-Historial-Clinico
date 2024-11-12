package app.backendclinic.user.controllers;
import java.util.List;

import app.backendclinic.Reportes.services.BitacoraService;
import app.backendclinic.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import app.backendclinic.dtos.RoleRequest;
import app.backendclinic.user.models.Role;
import app.backendclinic.user.repositorys.RoleRepository;
import app.backendclinic.user.services.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BitacoraService bitacoraService;

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    @GetMapping
    public List<Role> getAllRoles(HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();

        try {
            List<Role> roles = roleRepository.findAll();
            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA TODOS LOS ROLES", ipAddress, "Exitoso");
            return roles;
        } catch (Exception e) {
            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA TODOS LOS ROLES", ipAddress, "Fallido");
            throw e;
        }
    }
    @PutMapping("/{id}")
    public Role updateRole(@PathVariable String id, @RequestBody RoleRequest roleRequest, HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();

        try {
            Role updatedRole = roleService.updateRole(id, roleRequest);

            // Registro en bitácora de la acción de actualización
            try {
                bitacoraService.registrarAccion("ACTUALIZACIÓN", usuario, "ACTUALIZACIÓN DE ROLE", ipAddress, "Exitoso");
            } catch (Exception e) {
                System.err.println("Error al registrar la bitácora: " + e.getMessage());
            }
            return updatedRole;
        } catch (Exception e) {
            try {
                bitacoraService.registrarAccion("ACTUALIZACIÓN", usuario, "ACTUALIZACIÓN DE ROLE", ipAddress, "Fallido");
            } catch (Exception bitacoraError) {
                System.err.println("Error al registrar la bitácora en fallo: " + bitacoraError.getMessage());
            }
            throw e;
        }
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable String id, HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();

        try {
            Role role = roleRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Role no encontrado"));
            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA ROLE POR ID", ipAddress, "Exitoso");
            return role;
        } catch (Exception e) {
            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA ROLE POR ID", ipAddress, "Fallido");
            throw e;
        }
    }

    @PostMapping
    public Role createRole(@RequestBody RoleRequest roleRequest, HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();

        try {
            Role nuevoRole = roleService.createRole(roleRequest);

            // Manejo de errores en el registro de bitácora
            try {
                bitacoraService.registrarAccion("CREACIÓN", usuario, "CREACIÓN DE ROLE", ipAddress, "Exitoso");
            } catch (Exception e) {
                // Loguea el error sin afectar el flujo principal
                System.err.println("Error al registrar la bitácora: " + e.getMessage());
            }
            return nuevoRole;
        } catch (Exception e) {
            // Registramos en bitácora que la creación falló
            try {
                bitacoraService.registrarAccion("CREACIÓN", usuario, "CREACIÓN DE ROLE", ipAddress, "Fallido");
            } catch (Exception bitacoraError) {
                System.err.println("Error al registrar la bitácora en fallo: " + bitacoraError.getMessage());
            }
            throw e;
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable String id, HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();

        try {
            roleService.deleteRole(id);
            bitacoraService.registrarAccion("ELIMINACIÓN", usuario, "ELIMINACIÓN DE ROLE", ipAddress, "Exitoso");
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            try {
                bitacoraService.registrarAccion("ELIMINACIÓN", usuario, "ELIMINACIÓN DE ROLE", ipAddress, "Fallido");
            } catch (Exception bitacoraError) {
                System.err.println("Error al registrar en bitácora en fallo: " + bitacoraError.getMessage());
            }
            throw e;
        }
    }
}
