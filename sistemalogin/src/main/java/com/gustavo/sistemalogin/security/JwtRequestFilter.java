package com.gustavo.sistemalogin.security;

import com.gustavo.sistemalogin.service.UserDetailsServiceImpl;
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
 * Filtro de segurança que intercepta todas as requisições para validar o token JWT.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserDetailsServiceImpl userDetailsService;

    // Injeção de dependências via construtor (melhor prática)
    public JwtRequestFilter(TokenService tokenService, UserDetailsServiceImpl userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            var subject = tokenService.getSubject(tokenJWT);
            if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Aqui agora chama o novo serviço
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(subject); // <-- MUDANÇA AQUI

                if (tokenService.validateToken(tokenJWT, userDetails)) {
                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Extrai o token JWT do cabeçalho "Authorization".
     * @param request A requisição HTTP.
     * @return O token JWT como uma String, ou null se não for encontrado.
     */
    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            // Remove o prefixo "Bearer " para obter apenas o token
            return authorizationHeader.substring(7);
        }
        return null;
    }
}

