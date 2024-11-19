package app.backendclinic.jwt;

import java.io.IOException;

import io.jsonwebtoken.ExpiredJwtException;
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
        final String identifier;

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            identifier = jwtService.getUsernameFromToken(token);

            if (identifier != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = null;

                // Intentar cargar como usuario
                try {
                    userDetails = userDetailsService.loadUserByUsername(identifier);
                } catch (UsernameNotFoundException e) {
                    // Intentar cargar como cliente
                    Paciente cliente = pacienteService.findByEmail(identifier);
                    if (cliente != null) {
                        userDetails = new CustomUserDetails(cliente);
                    }
                }

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

        } catch (ExpiredJwtException ex) {
            // Token expirado
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Código 401
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"TokenExpired\",\"message\":\"El token ha expirado\"}");
            return;
        } catch (Exception ex) {
            // Otros errores
            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // Código 403
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"InvalidToken\",\"message\":\"Token inválido\"}");
            return;
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

