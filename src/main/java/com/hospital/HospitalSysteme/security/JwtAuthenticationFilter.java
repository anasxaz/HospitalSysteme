package com.hospital.HospitalSysteme.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService userDetailsService) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Récupérer le token JWT de la requête
        String token = getJwtFromRequest(request);

        logger.info("URI demandée : " + request.getRequestURI());
        logger.info("Token présent : " + (token != null));

        // Valider le token
        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            try {
                // Récupérer l'email de l'utilisateur à partir du token
                String email = tokenProvider.getUserEmailFromToken(token);
                logger.info("Email extrait du token : " + email);

                // Récupérer le rôle à partir du token
                String role = tokenProvider.getRoleFromToken(token);
                logger.info("Rôle extrait du token : " + role);

                // Charger l'utilisateur associé au token
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                logger.info("Autorités de l'utilisateur : " + userDetails.getAuthorities());

                // Créer l'authentification
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Définir l'authentification dans le contexte de sécurité
                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.info("Authentification définie dans le contexte de sécurité");
            } catch (Exception e) {
                logger.error("Erreur lors de l'authentification : ", e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}