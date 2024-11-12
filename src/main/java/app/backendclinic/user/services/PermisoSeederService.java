package app.backendclinic.user.services;

import app.backendclinic.models.User;
import app.backendclinic.repositorys.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import app.backendclinic.user.models.Permiso;
import app.backendclinic.user.models.Role;

import app.backendclinic.user.repositorys.PermisoRepository;
import app.backendclinic.user.repositorys.RoleRepository;


import java.util.List;

@Service
public class PermisoSeederService {

    @Autowired
    private PermisoRepository permisoRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initializeData() {
        initializePermissions();
        initializeAdminRoleAndUser();
    }

    private void initializePermissions() {
        Permiso[] permisos = {
                createPermiso("CREAR_PACIENTES", "Permiso para registrar nuevos pacientes"),
                createPermiso("VER_USUARIOS", "Permiso para ver usuarios"),
                createPermiso("EDITAR_USUARIOS", "Permiso para editar la información de usuarios"),
                createPermiso("ELIMINAR_USUARIOS", "Permiso para eliminar usuarios"),
                createPermiso("CREAR_USUARIOS", "Permiso para registrar nuevos usuarios"),
                createPermiso("VER_ROLES", "Permiso para ver roles"),
                createPermiso("EDITAR_ROLES", "Permiso para editar roles"),
                createPermiso("ELIMINAR_ROLES", "Permiso para eliminar roles"),
                createPermiso("CREAR_ROLES", "Permiso para registrar nuevos roles"),
                createPermiso("VER_PERMISOS", "Permiso para ver permisos"),
                createPermiso("ASIGNAR_PERMISOS", "Permiso para asignar permisos a roles"),
                createPermiso("VER_BITACORA", "Permiso para ver registros de bitácora"),
                createPermiso("VER_HISTORIAL_CLINICO", "Permiso para ver historiales clínicos de pacientes"),
                createPermiso("EDITAR_HISTORIAL_CLINICO", "Permiso para editar el historial clínico de un paciente"),
                createPermiso("VER_ANTECEDENTES", "Permiso para ver antecedentes de pacientes"),
                createPermiso("CREAR_ANTECEDENTES", "Permiso para registrar antecedentes médicos"),
                createPermiso("EDITAR_ANTECEDENTES", "Permiso para modificar antecedentes médicos"),
                createPermiso("VER_DATOS_ENFERMERIA", "Permiso para ver datos de enfermería de pacientes"),
                createPermiso("CREAR_DATOS_ENFERMERIA", "Permiso para registrar datos de enfermería"),
                createPermiso("EDITAR_DATOS_ENFERMERIA", "Permiso para modificar datos de enfermería"),
                createPermiso("VER_TRATAMIENTOS", "Permiso para ver tratamientos y anotaciones médicas"),
                createPermiso("CREAR_TRATAMIENTOS", "Permiso para registrar tratamientos y anotaciones"),
                createPermiso("EDITAR_TRATAMIENTOS", "Permiso para modificar tratamientos"),
                createPermiso("VER_HORARIOS_MEDICOS", "Permiso para ver horarios de atención de médicos"),
                createPermiso("ASIGNAR_HORARIOS", "Permiso para asignar horarios de atención"),
                createPermiso("VER_ESPECIALIDADES_MEDICAS", "Permiso para ver especialidades médicas"),
                createPermiso("CREAR_ESPECIALIDADES_MEDICAS", "Permiso para registrar especialidades médicas"),
                createPermiso("EDITAR_ESPECIALIDADES_MEDICAS", "Permiso para modificar especialidades médicas"),
                createPermiso("ELIMINAR_ESPECIALIDADES_MEDICAS", "Permiso para eliminar especialidades médicas")
        };

        for (Permiso permiso : permisos) {
            if (!permisoRepository.existsByName(permiso.getName())) {
                permisoRepository.save(permiso);
            }
        }
    }

    private Permiso createPermiso(String name, String description) {
        Permiso permiso = new Permiso();
        permiso.setName(name);
        permiso.setDescription(description);
        return permiso;
    }

    private void initializeAdminRoleAndUser() {
        // Verificar si el rol de Administrador ya existe
        Role adminRole = roleRepository.findByName("Administrador").orElse(null);
        if (adminRole == null) {
            // Crear el rol de Administrador y asignar permisos
            adminRole = new Role();
            adminRole.setName("Administrador");

            // Asignar un conjunto de permisos clave al rol de Administrador
            List<Permiso> permisosAdmin = permisoRepository.findAllByNameIn(List.of(
                    "VER_USUARIOS", "EDITAR_USUARIOS", "ELIMINAR_USUARIOS", "CREAR_USUARIOS",
                    "VER_ROLES", "EDITAR_ROLES", "ELIMINAR_ROLES", "CREAR_ROLES",
                    "ASIGNAR_PERMISOS", "VER_BITACORA", "VER_HISTORIAL_CLINICO", "EDITAR_HISTORIAL_CLINICO",
                    "VER_DATOS_ENFERMERIA", "EDITAR_DATOS_ENFERMERIA", "VER_ANTECEDENTES",
                    "CREAR_ANTECEDENTES", "EDITAR_ANTECEDENTES", "VER_TRATAMIENTOS", "EDITAR_TRATAMIENTOS"
            ));
            adminRole.setPermissions(permisosAdmin);
            roleRepository.save(adminRole);
        }

        // Crear el usuario administrador solo si no existe
        boolean adminExists = userRepository.existsByUsername("admin@clinic.com");
        if (!adminExists) {
            User adminUser = User.builder()
                    .username("admin@clinic.com")
                    .password(passwordEncoder.encode("123456"))
                    .nombre("Admin")
                    .apellido("User")
                    .telefono("123456789")
                    .role(adminRole)
                    .isActive(true)
                    .build();
            userRepository.save(adminUser);
        }
    }
}
