package com.gustavo.sistemalogin.security;

import com.gustavo.sistemalogin.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que intercepta todas as requisições para validar o token JWT.
 * Ele é executado uma vez por requisição.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtRequestFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Extrai o cabeçalho "Authorization" da requisição.
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 2. Verifica se o cabeçalho existe e se começa com "Bearer ".
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Extrai apenas o token, removendo o prefixo "Bearer ".
            jwt = authorizationHeader.substring(7);
            // Extrai o email (username) de dentro do token.
            username = jwtUtil.extractUsername(jwt);
        }

        // 3. Se o email foi extraído e ainda não há autenticação no contexto de segurança atual.
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carrega os detalhes do usuário a partir do banco de dados.
            UserDetails userDetails = this.userService.loadUserByUsername(username);

            // 4. Se o token for válido para este usuário.
            if (jwtUtil.validateToken(jwt)) {
                // Cria o objeto de autenticação.
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 5. Coloca a autenticação no contexto de segurança do Spring.
                // A partir deste ponto, o usuário está autenticado para esta requisição.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // 6. Continua a cadeia de filtros.
        filterChain.doFilter(request, response);
    }
}
