package app.backendclinic.user.controllers;
import java.util.List;

import app.backendclinic.Reportes.services.BitacoraService;
import app.backendclinic.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            bitacoraService.registrarAccion("CREACIÓN", usuario, "CREACIÓN DE ROLE", ipAddress, "Exitoso");
            return nuevoRole;
        } catch (Exception e) {
            bitacoraService.registrarAccion("CREACIÓN", usuario, "CREACIÓN DE ROLE", ipAddress, "Fallido");
            throw e;
        }
    }
}
