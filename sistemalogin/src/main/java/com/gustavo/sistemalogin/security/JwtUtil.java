package com.gustavo.sistemalogin.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Utilitário para geração e validação de JWT usando Nimbus JOSE + JWT.
 */
@Component
public class JwtUtil {

    // Valor do segredo para assinar o token (vem do application.properties/yaml)
    @Value("${jwt.secret}")
    private String secret;

    // Tempo de expiração do token: 1 hora (em milissegundos)
    private static final long EXPIRATION_TIME = 60 * 60 * 1000;

    /**
     * Gera um token JWT com o email do usuário.
     * @param email email do usuário
     * @return token JWT assinado como String
     */
    public String generateToken(String email) {
        try {
            JWSSigner signer = new MACSigner(secret.getBytes());
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(email)
                    .issueTime(new Date())
                    .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .build();

            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet
            );

            signedJWT.sign(signer);

            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar JWT", e);
        }
    }

    /**
     * Valida o token JWT (verifica assinatura e expiração).
     *
     * @param token       JWT recebido
     * @return true se válido, false se inválido/expirado
     */
    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret.getBytes());

            return signedJWT.verify(verifier) && !isTokenExpired(signedJWT);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extrai o email (subject) do token JWT.
     * @param token JWT recebido
     * @return email do usuário, ou null se token inválido
     */
    public String extractUsername(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret.getBytes());

            if (signedJWT.verify(verifier)) {
                return signedJWT.getJWTClaimsSet().getSubject();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Valida o token e retorna o email do usuário se válido.
     * @param token JWT recebido
     * @return email do usuário se válido, senão null
     */
    public String validateTokenAndGetEmail(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret.getBytes());

            if (signedJWT.verify(verifier) && !isTokenExpired(signedJWT)) {
                return signedJWT.getJWTClaimsSet().getSubject();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Verifica se o token está expirado.
     * @param jwt objeto JWT assinado
     * @return true se expirado, false se válido
     */
    private boolean isTokenExpired(SignedJWT jwt) throws Exception {
        Date expiration = jwt.getJWTClaimsSet().getExpirationTime();
        return expiration == null || expiration.before(new Date());
    }
}