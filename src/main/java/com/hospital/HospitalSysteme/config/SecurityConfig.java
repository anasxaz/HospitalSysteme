package com.hospital.HospitalSysteme.config;

import com.hospital.HospitalSysteme.security.CustomUserDetailsService;
import com.hospital.HospitalSysteme.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Ajouter cette annotation pour activer @PreAuthorize
//@EnableMethodSecurity(prePostEnabled = true) // Ajouter cette annotation pour activer @PreAuthorize
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        // Permettre l'accès à Swagger et à la documentation API
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", "/api-docs/**").permitAll()
                        // Permettre l'accès aux endpoints d'authentification
                        .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/password-reset-request", "/api/auth/password-reset/**").permitAll()
                        // Permettre l'accès à l'inscription des patients
                        .requestMatchers("/api/patients/register").permitAll()
                        // Pour authentification du patient
                        .requestMatchers("/api/patient-registration").permitAll()
                        // Un test ici
                        .requestMatchers("/api/test/public").permitAll()
                        // Un autre test :
                        .requestMatchers("/api/patients/test").permitAll()
                        // Juste un test
                        .requestMatchers("/api/medecins/public-test").permitAll()
                        // Juste pour tester
//                        .requestMatchers("/api/medecins").permitAll()
                        // Juste pour tester de créer un médecin
                        .requestMatchers("/api/medecins/test-create").permitAll()
                        // TEMPORAIRE: Permettre l'accès à tous les endpoints de médecin et département
//                        .requestMatchers("/api/medecins/**", "/api/departements/**").permitAll()
                        // Exiger une authentification pour tous les autres endpoints
                        .anyRequest().authenticated()
                );

        // Ajouter le filtre JWT
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}