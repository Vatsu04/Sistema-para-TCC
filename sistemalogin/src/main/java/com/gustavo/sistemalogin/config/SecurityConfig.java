package com.gustavo.sistemalogin.config;

import com.gustavo.sistemalogin.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso público para login e registo
                        .requestMatchers("/api/auth/**").permitAll()
                        // Para todo o resto, basta estar autenticado. O controlo fino será feito nos controllers.
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


/*    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso público a todas as rotas de autenticação
                        .requestMatchers("/api/auth/**").permitAll()

                        // --- REGRAS REFINADAS AQUI ---
                        // Para Pessoas, permite qualquer método (GET, POST, etc.) se autenticado
                        .requestMatchers(HttpMethod.GET, "/api/pessoas", "/api/pessoas/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/pessoas").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/pessoas/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/pessoas/**").authenticated()

                        // Para Funis, permite qualquer método se autenticado
                        .requestMatchers("/api/funis/**").authenticated()

                        // Adicione regras para negócios aqui...
                        .requestMatchers("/api/negocios/**").authenticated()

                        // Exige que qualquer outra requisição seja autenticada
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }*/

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

