package com.gustavo.sistemalogin.security;

import com.gustavo.sistemalogin.model.User;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;

/**
 * Serviço para geração e validação de JWT usando Nimbus JOSE + JWT.
 */
@Service // Alterado de @Component para @Service para maior clareza semântica
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    private static final long EXPIRATION_TIME = 60 * 60 * 1000; // 1 hora

    public String generateToken(String email) {
        try {
            JWSSigner signer = new MACSigner(secret.getBytes());
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(email)
                    .issuer("API FastCRM") // Adicionado um emissor para maior segurança
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    /**
     * Extrai o email (subject) do token JWT.
     * Este método substitui o antigo 'extractUsername'.
     * @param token JWT recebido
     * @return email do usuário
     * @throws RuntimeException se o token for inválido ou não puder ser processado
     */
    public String getSubject(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret.getBytes());

            if (signedJWT.verify(verifier)) {
                System.out.println("Subject extracted: " + token);
                return signedJWT.getJWTClaimsSet().getSubject();
            } else {
                throw new RuntimeException("Assinatura do token JWT é inválida!");
            }
        } catch (JOSEException | ParseException e) {
            throw new RuntimeException("Token JWT inválido ou expirado!", e);
        }
    }

    public String gerarToken(User user) {
        try {
            // 1. Cria o "assinador" do token usando o seu segredo e o algoritmo HS256
            JWSSigner signer = new MACSigner(secret.getBytes());

            // 2. Define as "reivindicações" (claims) do token: quem é o dono, quando expira, etc.
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(user.getEmail()) // O "dono" do token é o email do utilizador
                    .issuer("API FastCRM")     // Quem emitiu o token
                    .issueTime(new Date())   // Data de emissão
                    .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Data de expiração
                    .build();

            // 3. Cria o objeto JWT com o cabeçalho e as reivindicações
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet
            );

            // 4. Assina o JWT com o segredo
            signedJWT.sign(signer);

            // 5. Retorna o token como uma string compacta e segura
            return signedJWT.serialize();
        } catch (JOSEException e) {
            // Lança uma exceção se algo correr mal durante a geração
            throw new RuntimeException("Erro ao gerar token JWT", e);
        }
    }

    /**
     * Valida o token JWT (verifica assinatura, expiração e correspondência de usuário).
     * @param token JWT recebido
     * @param userDetails Detalhes do usuário para verificação
     * @return true se válido
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = getSubject(token);
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret.getBytes());
            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(signedJWT)
                    && signedJWT.verify(verifier);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica se o token está expirado.
     * @param jwt objeto JWT assinado
     * @return true se expirado, false se válido
     */
    private boolean isTokenExpired(SignedJWT jwt) throws ParseException {
        Date expiration = jwt.getJWTClaimsSet().getExpirationTime();
        return expiration != null && expiration.before(new Date());
    }

    // O método problemático 'getSubject' que usava 'com.auth0.jwt' foi removido.
}

