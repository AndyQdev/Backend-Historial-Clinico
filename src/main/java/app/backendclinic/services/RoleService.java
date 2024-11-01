package app.backendclinic.services;


import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.backendclinic.dtos.RoleRequest;
import app.backendclinic.models.Permiso;
import app.backendclinic.models.Role;
import app.backendclinic.repositorys.PermisoRepository;
import app.backendclinic.repositorys.RoleRepository;


@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermisoRepository permisoRepository;

    public Role createRole(RoleRequest roleRequest) {
        // 1. Buscar los permisos por los IDs proporcionados
        List<Permiso> permisos = new ArrayList<>();
        for (String permisoId : roleRequest.getPermissionsId()) {
            permisoRepository.findById(permisoId).ifPresent(permisos::add);
        }
        System.out.println(permisos);
        // 2. Crear un nuevo Role
        Role role = Role.builder()
                        .id(UUID.randomUUID().toString()) // Generar un UUID para el Role
                        .name(roleRequest.getName())
                        .permissions(permisos)
                        .build();

        // 3. Guardar el Role en la base de datos
        return roleRepository.save(role);
    }
}
