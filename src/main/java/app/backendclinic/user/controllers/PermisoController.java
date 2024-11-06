package app.backendclinic.user.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.backendclinic.user.models.Permiso;
import app.backendclinic.user.repositorys.PermisoRepository;



@RestController
@RequestMapping("/api/permiso")
public class PermisoController {
        @Autowired
    private PermisoRepository permisoRepository;

    @GetMapping
    public List<Permiso> getAllPermisos(){
        return permisoRepository.findAll();
    }

    @GetMapping("/{id}")
    public Permiso getAllPermisoById(@RequestBody String id){
        return permisoRepository.findById(id).get();
    }
    
    @PostMapping
    public Permiso createPermiso(@RequestBody Permiso permiso){
        return permisoRepository.save(permiso);
    }
}
