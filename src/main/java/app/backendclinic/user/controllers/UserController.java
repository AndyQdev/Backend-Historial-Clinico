package app.backendclinic.user.controllers;

import java.util.List;

import app.backendclinic.Reportes.services.BitacoraService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.backendclinic.models.User;
import app.backendclinic.repositorys.UserRepository;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BitacoraService bitacoraService;

    private User getAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }

    @GetMapping
    public List<User> getAllUsers(HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();

        try {
            List<User> users = userRepository.findAll();
            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA TODOS LOS USUARIOS", ipAddress, "Exitoso");
            return users;
        } catch (Exception e) {
            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA TODOS LOS USUARIOS", ipAddress, "Fallido");
            throw e;
        }
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable String id, HttpServletRequest request) {
        User usuario = getAuthenticatedUser();
        String ipAddress = request.getRemoteAddr();

        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA USUARIO POR ID", ipAddress, "Exitoso");
            return user;
        } catch (Exception e) {
            bitacoraService.registrarAccion("VISTA", usuario, "CONSULTA USUARIO POR ID", ipAddress, "Fallido");
            throw e;
        }
    }
}
