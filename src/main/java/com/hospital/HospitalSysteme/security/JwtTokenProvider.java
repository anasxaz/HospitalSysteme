//package com.hospital.HospitalSysteme.security;
//
//import com.hospital.HospitalSysteme.entity.User;
//import com.hospital.HospitalSysteme.exception.ResourceNotFoundException;
//import com.hospital.HospitalSysteme.repository.UserRepository;
//import io.jsonwebtoken.*;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//@Component
//public class JwtTokenProvider {
//
//    @Value("${app.jwt.secret}")
//    private String jwtSecret;
//
//    @Value("${app.jwt.expiration-milliseconds}")
//    private long jwtExpirationInMs;
//
//    private final UserRepository userRepository;
//
//    public JwtTokenProvider(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }
//
//
//
//    // Pour stocker les tokens invalidés (déconnexion)
//    private Set<String> invalidatedTokens = ConcurrentHashMap.newKeySet();
//
//    // Générer un token JWT
//    public String generateToken(Authentication authentication) {
//        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        // Récupérer l'utilisateur depuis le repository
//        User user = userRepository.findByEmail(userDetails.getUsername())
//                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
//
//        Date currentDate = new Date();
//        Date expiryDate = new Date(currentDate.getTime() + jwtExpirationInMs);
//
//        // Claims supplémentaires
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("username", userDetails.getUsername());
//        claims.put("userId", user.getId().toString());
//        claims.put("role", user.getProfil().name());
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(userDetails.getUsername())
//                .setIssuedAt(currentDate)
//                .setExpiration(expiryDate)
//                .signWith(getSigningKey())
//                .compact();
//    }
//
//    // Valider un token JWT
//    public boolean validateToken(String token) {
//        if (invalidatedTokens.contains(token)) {
//            return false;
//        }
//
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(getSigningKey())
//                    .build()
//                    .parseClaimsJws(token);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//    // Invalider un token (lors de la déconnexion)
//    public void invalidateToken(String token) {
//        invalidatedTokens.add(token);
//    }
//
//    // Extraire l'ID utilisateur du token
//    public Long getUserIdFromToken(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        return Long.parseLong(claims.get("userId", String.class));
//    }
//
//    // Extraire le rôle du token
//    public String getRoleFromToken(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(getSigningKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        return claims.get("role", String.class);
//    }
//
//    private Key getSigningKey() {
//        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
//    }
//}














package com.hospital.HospitalSysteme.security;

import com.hospital.HospitalSysteme.entity.User;
import com.hospital.HospitalSysteme.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtTokenProvider {

    @Autowired
    private UserRepository userRepository;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-milliseconds}")
    private long jwtExpirationInMs;

    // Stockage des tokens invalidés (pour la déconnexion)
    private Set<String> invalidatedTokens = ConcurrentHashMap.newKeySet();

    // Générer un token
    public String generateToken(Authentication authentication) throws NoSuchAlgorithmException {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Date currentDate = new Date();
        Date expiryDate = new Date(currentDate.getTime() + jwtExpirationInMs);

        Map<String, Object> claims = new HashMap<>();
        // Vous pouvez ajouter des claims supplémentaires ici
        claims.put("sub", userDetails.getUsername());
        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority());
//        claims.put("role", userDetails.getAuthorities().iterator().next().getAuthority().replace("ROLE_", ""));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(currentDate)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // Obtenir l'ID utilisateur à partir du token
    // Obtenir l'ID utilisateur à partir du token
    public Long getUserIdFromToken(String token) throws NoSuchAlgorithmException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        String username = claims.getSubject();
        // Récupérer l'utilisateur depuis le repository en utilisant l'email
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + username));

        return user.getId();
    }

    // Obtenir le rôle à partir du token
    public String getRoleFromToken(String token) throws NoSuchAlgorithmException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    // Valider un token
    public boolean validateToken(String token) {
        try {
            // Vérifier si le token a été invalidé (déconnexion)
            if (invalidatedTokens.contains(token)) {
                return false;
            }

            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Invalider un token (pour la déconnexion)
    public void invalidateToken(String token) {
        invalidatedTokens.add(token);
    }

    private Key getSigningKey() throws NoSuchAlgorithmException {
        // Si la clé est trop courte, on utilise un hachage pour l'étendre
        byte[] keyBytes = jwtSecret.getBytes();
        byte[] hashedKey = java.security.MessageDigest.getInstance("SHA-512").digest(keyBytes);
        return Keys.hmacShaKeyFor(hashedKey);
    }

    // Ajouter cette méthode à la classe JwtTokenProvider
    public String getUserEmailFromToken(String token) throws NoSuchAlgorithmException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}