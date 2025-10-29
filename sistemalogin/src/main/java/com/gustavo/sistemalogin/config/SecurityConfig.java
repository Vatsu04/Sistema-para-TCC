package com.gustavo.sistemalogin.config;

import com.gustavo.sistemalogin.security.JwtRequestFilter;
import com.gustavo.sistemalogin.security.TokenService;
import com.gustavo.sistemalogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import com.gustavo.sistemalogin.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.DisabledException;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Permite acesso público para todas as rotas de autenticação
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/oauth2/**").permitAll()
                        .requestMatchers("/login/oauth2/code/*").permitAll()


                        // Para todo o resto, basta estar autenticado.
                        .anyRequest().authenticated()
                )

                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                // Este é o link que o frontend chamará
                                .baseUri("/oauth2/authorization")
                        )
                        .redirectionEndpoint(endpoint -> endpoint
                                // Este é o link que o Google chamará de volta
                                .baseUri("/login/oauth2/code/*")
                        )
                        // Usa o seu handler customizado para gerar o JWT
                        .successHandler(oAuth2AuthenticationSuccessHandler())
                )
                // --- FIM DAS ADIÇÕES ---

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
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Permite requisições de qualquer origem. Para produção, restrinja a `http://seusite.com`.
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
            String email = oAuth2User.getAttribute("email");

            String frontendLoginUrl = "http://127.0.0.1:5500/sistemalogin/frontend/front%20end/index.html";

            try {

                User user = userService.findAndValidateUserForOAuth(email);
                String jwtToken = tokenService.gerarToken(user);

                String targetUrl = "http://127.0.0.1:5500/sistemalogin/frontend/front%20end/login-success.html?token=" + jwtToken;
                response.sendRedirect(targetUrl);

            } catch (UsernameNotFoundException e) {
                response.sendRedirect(frontendLoginUrl + "?error=oauth_user_not_found");
            } catch (DisabledException e) {
                response.sendRedirect(frontendLoginUrl + "?error=oauth_user_inactive");
            }
        };
    }

    /*    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }*/
}

