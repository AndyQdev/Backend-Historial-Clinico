package app.backendclinic.controllers;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.backendclinic.dtos.RoleRequest;
import app.backendclinic.models.Role;
import app.backendclinic.repositorys.RoleRepository;
import app.backendclinic.services.RoleService;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleService roleService;
    @GetMapping
    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    @GetMapping("/{id}")
    public Role getRoleById(@PathVariable String id) {
        return roleRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + id));
    }
    
    @PostMapping
    public Role createUser(@RequestBody RoleRequest role){
        return roleService.createRole(role);
    }
}
