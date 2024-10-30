package app.backendclinic.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.backendclinic.jwt.JwtService;
import app.backendclinic.models.Paciente;
import app.backendclinic.models.Role;
import app.backendclinic.models.Usuario;
import lombok.RequiredArgsConstructor;
import app.backendclinic.repositorys.PacienteRepository;
import app.backendclinic.repositorys.RoleRepository;
import app.backendclinic.repositorys.UserRepository;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository; 
    private final PacienteRepository pacienteRepository; 
    

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        String token = jwtService.getToken(user);
        return AuthResponse.builder()
        .token(token)
        .build();
    }
    public AuthResponse loginCliente(LoginRequest request) {
        Paciente paciente = pacienteRepository.findByEmail(request.getUsername()).orElseThrow();
        System.out.println(request);
        if (!passwordEncoder.matches(request.getPassword(), paciente.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        
        if (!paciente.getEmail().equals(request.getUsername())) {
            throw new IllegalArgumentException("Invalid email");
        }
        String token = jwtService.getTokenWithoutExpiration(paciente);
        return AuthResponse.builder()
        .token(token)
        .paciente(paciente)
        .build();
    }

    public AuthResponse registerPaciente(RegisterPaciente request) {
        Paciente paciente = Paciente.builder()
        .fechaNacimiento(request.getFechaNacimiento())
        .apellido(request.getApellido())
        .numeroSeguro(request.getNumeroSeguro())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .direccion(request.getDireccion())
        .telefono(request.getTelefono())
        .nombre(request.getNombre())
        .isActive(true)
        .build();
        
        pacienteRepository.save(paciente);
        return AuthResponse.builder()
        .token(jwtService.getTokenWithoutExpiration(paciente))
        .build();
    }
    public AuthResponse register(RegisterRequest request) {
        Role role = roleRepository.findById(request.getRoleId())
            .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        // Branch branch = branchRepository.findById(request.getBranchId())
        //     .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        Usuario user = Usuario.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .telefono(request.getTelefono())
        .role(role)
        .nombre(request.getNombre())
        .apellido(request.getApellido())
        // .branch(branch)
        .isActive(request.is_active)
        .build();
        
        userRepository.save(user);
        return AuthResponse.builder()
        .token(jwtService.getToken(user))
        .build();
    }

}
