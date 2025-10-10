package com.gustavo.sistemalogin.config;

import com.gustavo.sistemalogin.security.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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
                // 1. Desabilita o CSRF (Cross-Site Request Forgery)
                .csrf(csrf -> csrf.disable())

                // 2. Define a política de sessão como STATELESS (sem estado)
                // Essencial para APIs JWT, para não criar cookies de sessão (JSESSIONID)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configura as regras de autorização para os endpoints HTTP
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso público e sem autenticação a todas as rotas que começam com /api/auth/
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/pessoas/**").authenticated()
                        .requestMatchers("/api/funis/**").authenticated()
                        .requestMatchers("/api/negocios/**").authenticated()
                        // Exige que qualquer outra requisição seja autenticada
                        .anyRequest().authenticated()
                )
                // 4. Adiciona o nosso filtro de JWT para ser executado antes do filtro padrão de autenticação
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

