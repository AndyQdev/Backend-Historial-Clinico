package app.backendclinic.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import app.backendclinic.jwt.JwtService;
import app.backendclinic.models.User;
import app.backendclinic.pacientes.models.Paciente;
import app.backendclinic.pacientes.repositorys.PacienteRepository;
import lombok.RequiredArgsConstructor;
import app.backendclinic.repositorys.UserRepository;
import app.backendclinic.user.models.Role;
import app.backendclinic.user.repositorys.RoleRepository;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    // private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository; 
    private final PacienteRepository pacienteRepository; 
    private final TwilioService twilioService; // Servicio de Twilio para WhatsApp

    public AuthResponse login(LoginRequest request) {
        // Busca el usuario por username
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    
        // Verifica la contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
    
        // Genera el token JWT si la validación fue exitosa
        String token = jwtService.getToken(user);
    
        // Retorna la respuesta con el token
        return AuthResponse.builder()
                .token(token)
                .user(user)
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

    // public AuthResponse registerPaciente(RegisterPaciente request) {
    //     Paciente paciente = Paciente.builder()
    //     .fechaNacimiento(request.getFechaNacimiento())
    //     .apellido(request.getApellido())
    //     .numeroSeguro(request.getNumeroSeguro())
    //     .email(request.getEmail())
    //     .password(passwordEncoder.encode(request.getPassword()))
    //     .direccion(request.getDireccion())
    //     .telefono(request.getTelefono())
    //     .nombre(request.getNombre())
    //     .isActive(true)
    //     .build();
        
    //     pacienteRepository.save(paciente);
    //     return AuthResponse.builder()
    //     .token(jwtService.getTokenWithoutExpiration(paciente))
    //     .build();
    // }
    public AuthResponse registerPaciente(RegisterPaciente request) {
        String verificationCode = twilioService.generateVerificationCode(); // Genera el código de verificación

        Paciente paciente = Paciente.builder()
            .fechaNacimiento(request.getFechaNacimiento())
            .apellido(request.getApellido())
            .numeroSeguro(request.getNumeroSeguro())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .direccion(request.getDireccion())
            .telefono(request.getTelefono())
            .nombre(request.getNombre())
            .isActive(false) // Cuenta inactiva hasta verificar el código
            .verificationCode(verificationCode) // Almacena el código en el paciente
            .codeExpiration(LocalDateTime.now().plusMinutes(5)) // Expiración en 5 minutos
            .build();

        pacienteRepository.save(paciente);

        // Envía el código de verificación por WhatsApp
        twilioService.sendVerificationCode(paciente.getTelefono(), verificationCode);

        return AuthResponse.builder()
            .message("Registro exitoso. Código de verificación enviado.")
            .token(jwtService.getTokenWithoutExpiration(paciente))
            .paciente(paciente)
            .build();
    }
    
    public boolean verifyPaciente(String email, String code) {
        System.out.println(email);
        Optional<Paciente> pacienteOpt = pacienteRepository.findByEmail(email);
        System.out.println(pacienteOpt.isPresent());
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            if (paciente.getVerificationCode().equals(code) && 
                paciente.getCodeExpiration().isAfter(LocalDateTime.now())) {
                // Verificación exitosa, activar la cuenta
                paciente.setActive(true);
                paciente.setVerificationCode(null); // Limpia el código
                paciente.setCodeExpiration(null);
                pacienteRepository.save(paciente);
                return true;
            }
        }
        return false;
    }

    public boolean resendVerificationCode(String email) {
        System.out.println(email);
        Optional<Paciente> pacienteOpt = pacienteRepository.findByEmail(email);
        System.out.println(pacienteOpt.isPresent());
        if (pacienteOpt.isPresent()) {
            Paciente paciente = pacienteOpt.get();
            if (!paciente.isActive() || paciente.getCodeExpiration().isBefore(LocalDateTime.now())) {
                String newCode = twilioService.generateVerificationCode();
                paciente.setVerificationCode(newCode);
                paciente.setCodeExpiration(LocalDateTime.now().plusMinutes(5));
                pacienteRepository.save(paciente);
                twilioService.sendVerificationCode(paciente.getTelefono(), newCode);
                return true;
            }
        }
        return false;
    }
    public AuthResponse register(RegisterRequest request) {
        Role role = roleRepository.findById(request.getRoleId())
            .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        // Branch branch = branchRepository.findById(request.getBranchId())
        //     .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        User user = User.builder()
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
