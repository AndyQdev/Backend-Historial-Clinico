package app.backendclinic.jwt;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import app.backendclinic.models.CustomUserDetails;
import app.backendclinic.pacientes.models.Paciente;
import app.backendclinic.pacientes.services.PacienteService;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


//Intercepta cada solicitud HTTP: Cada vez que un cliente realiza una solicitud, este filtro se ejecuta antes de que la solicitud llegue a su destino final.
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService; // Para usuarios
    private final PacienteService pacienteService; // Servicio para manejar clientes

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String token = getTokenFromRequest(request);
        final String identifier; // Puede ser username (para usuarios) o email (para clientes)

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        identifier = jwtService.getUsernameFromToken(token); // Extraer el identificador (email o username)

        if (identifier != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = null;

            // Intentar cargar como usuario
            try {
                userDetails = userDetailsService.loadUserByUsername(identifier);
            } catch (UsernameNotFoundException e) {
                // Si no es usuario, intentamos cargar como cliente
                Paciente cliente = pacienteService.findByEmail(identifier); // Buscar el cliente por email
                if (cliente != null) {
                    userDetails = new CustomUserDetails(cliente); // Adaptar cliente a un UserDetails
                }
            }

            // Si encontramos un cliente o usuario y el token es válido
            if (userDetails != null && jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
}

