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
 * Filtro de segurança que intercepta todas as requisições para validar o token JWT.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserService userService;

    // Injeção de dependências via construtor (melhor prática)
    public JwtRequestFilter(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Tenta recuperar o token do cabeçalho da requisição
        var tokenJWT = recuperarToken(request);

        // 2. Se um token foi encontrado...
        if (tokenJWT != null) {
            // 3. Extrai o "subject" (email do utilizador) do token
            var subject = tokenService.getSubject(tokenJWT);

            // 4. Se o email foi extraído e ainda não há uma autenticação ativa para esta requisição...
            if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 5. Carrega os detalhes do utilizador da base de dados usando o email
                UserDetails userDetails = this.userService.loadUserByUsername(subject);

                // 6. Valida o token (assinatura, expiração, e se corresponde ao utilizador)
                if (tokenService.validateToken(tokenJWT, userDetails)) {
                    // 7. Se o token for válido, cria um objeto de autenticação para o Spring Security
                    var authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 8. Define o utilizador como autenticado no contexto de segurança.
                    // A partir daqui, o Spring sabe que a requisição é válida.
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }

        // 9. Continua o fluxo da requisição, passando para o próximo filtro ou para o controller
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

