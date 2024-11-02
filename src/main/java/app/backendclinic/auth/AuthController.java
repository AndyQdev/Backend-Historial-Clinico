package app.backendclinic.auth;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.backendclinic.jwt.JwtService;
import app.backendclinic.models.Paciente;
import app.backendclinic.repositorys.PacienteRepository;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {
    
    private final AuthService authService;
    private final JwtService jwtService; // Servicio que maneja los tokens
    private final PacienteRepository pacienteRepository; 

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }
    
    @PostMapping(value = "/login-paciente")
    public ResponseEntity<AuthResponse> loginCliente(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.loginCliente(request));
    }

    @PostMapping(value = "/register-paciente")
    public ResponseEntity<AuthResponse> registerCliente(@RequestBody RegisterPaciente request){
        return ResponseEntity.ok(authService.registerPaciente(request));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyPaciente(@RequestBody VerificationRequest request) {
        boolean isVerified = authService.verifyPaciente(request.getEmail(), request.getCode());
        if (isVerified) {
            return ResponseEntity.ok("Verificación exitosa. La cuenta ha sido activada.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Código de verificación inválido o expirado.");
        }
    }
    
    @PostMapping("/resend-code")
    public ResponseEntity<?> resendVerificationCode(@RequestBody String email) {
        boolean isResent = authService.resendVerificationCode(email);
        if (isResent) {
            return ResponseEntity.ok("Nuevo código de verificación enviado.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se pudo enviar el código. Verifique el email.");
        }
    }
    
    @PostMapping(value = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }
    
    // Verificación de token
    @GetMapping("/check-token")
    public ResponseEntity<?> checkToken(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Eliminar el prefijo 'Bearer '
        }

        // Verificar si el token es válido
        if (jwtService.isTokenValid(token)) {
            String username = jwtService.getUsernameFromToken(token); // Utiliza getUsernameFromToken
            return ResponseEntity.ok(Map.of("username", username)); // Retorna la información del usuario
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
        }
    }

    // Verificación de token para clientes
    @GetMapping("/check-token-cliente")
    public ResponseEntity<?> checkTokenCliente(@RequestHeader("Authorization") String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7); // Eliminar el prefijo 'Bearer '
        }

        // Verificar si el token es válido
        if (jwtService.isTokenValid(token)) {
            String email = jwtService.getUsernameFromToken(token); // Extrae el email en lugar de username
            return ResponseEntity.ok(Map.of("email", email)); // Retorna la información del cliente
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido o expirado");
        }
    }

    @GetMapping("/check-existe-cliente")
    public ResponseEntity<?> checkUserExists(@RequestParam("email") String email) {
        System.out.println(email);
        Optional<Paciente> existingClient = pacienteRepository.findByEmail(email);
        if (existingClient.isPresent()) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }
    }
}
