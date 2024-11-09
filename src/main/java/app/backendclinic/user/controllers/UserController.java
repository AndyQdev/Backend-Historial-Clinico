package app.backendclinic.user.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping
    public List<User> getAllRoles(){
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User getRoleById(@PathVariable String id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id));
    }
}
