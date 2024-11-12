package app.backendclinic.user.services;


import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.backendclinic.dtos.RoleRequest;
import app.backendclinic.user.models.Permiso;
import app.backendclinic.user.models.Role;
import app.backendclinic.user.repositorys.PermisoRepository;
import app.backendclinic.user.repositorys.RoleRepository;


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

    public Role updateRole(String id, RoleRequest roleRequest) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role no encontrado"));

        // Actualizar el nombre y los permisos del rol existente
        existingRole.setName(roleRequest.getName());

        List<Permiso> permisos = new ArrayList<>();
        for (String permisoId : roleRequest.getPermissionsId()) {
            permisoRepository.findById(permisoId).ifPresent(permisos::add);
        }
        existingRole.setPermissions(permisos);

        return roleRepository.save(existingRole);
    }
    public void deleteRole(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role no encontrado"));
        roleRepository.delete(role);
    }
}
