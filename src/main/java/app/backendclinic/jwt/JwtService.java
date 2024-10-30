package app.backendclinic.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map; // Este es el Map correcto
import java.util.function.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import app.backendclinic.models.Paciente;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private static final String SECRET_KEY = "523ASDASFDSDF21SAD53SAD5AS43D5S3D5ASD512JKJOQONQIWUBQUBWEBU";

    // Generar token para UserDetails
    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user.getUsername());
    }

    private String getToken(Map<String, Object> extraClaims, String subject) {
        return Jwts
               .builder()
               .setClaims(extraClaims)
               .setSubject(subject)
               .setIssuedAt(new Date(System.currentTimeMillis()))
               .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // Expira en 24 minutos
               .signWith(getKey(), SignatureAlgorithm.HS256)
               .compact();
    }

    // Generar token sin expiración para clientes
    public String getTokenWithoutExpiration(Paciente paciente) {
        return Jwts
               .builder()
               .setClaims(new HashMap<>())
               .setSubject(paciente.getEmail())
               .setIssuedAt(new Date(System.currentTimeMillis())) 
               .signWith(getKey(), SignatureAlgorithm.HS256)
               .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Obtener el sujeto (username o email) del token
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    // Validar token para UserDetails
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Validar token en general
    public boolean isTokenValid(String token) {
        try {
            System.out.println("Validando token");
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            System.out.println("Token válido");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Obtener los claims
    private Claims getAllClaims(String token) {
        return Jwts
               .parserBuilder()
               .setSigningKey(getKey())
               .build()
               .parseClaimsJws(token)
               .getBody();
    }

    // Obtener claim específico
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        try {
            return getClaim(token, Claims::getExpiration);
        } catch (Exception e) {
            // Si no se puede obtener la expiración, devolvemos null
            return null;
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = getExpiration(token);
        
        // Si el token no tiene fecha de expiración, significa que no expira (token para clientes)
        if (expiration == null) {
            return false; // El token es válido indefinidamente
        }
    
        return expiration.before(new Date());
    }

}
