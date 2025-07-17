package com.gustavo.sistemalogin.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component // Indica que essa classe pode ser injetada como um componente Spring
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
            // Cria um objeto responsável por assinar o token usando o segredo
            JWSSigner signer = new MACSigner(secret.getBytes());
            // Define as informações (claims) do token
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(email) // e-mail do usuário vai no subject
                    .issueTime(new Date()) // data de emissão
                    .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // expiração
                    .build();

            // Cria o token JWT assinado com header e claims
            SignedJWT signedJWT = new SignedJWT(
                    new JWSHeader(JWSAlgorithm.HS256),
                    claimsSet
            );

            // Assina o token
            signedJWT.sign(signer);

            // Retorna o token no formato compactado (string)
            return signedJWT.serialize();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar JWT", e);
        }
    }

    /**
     * Valida o token e retorna o email do usuário se válido.
     * @param token JWT recebido
     * @return email do usuário se válido, senão null
     */
    public String validateTokenAndGetEmail(String token) {
        try {
            // Faz o parse do token recebido
            SignedJWT signedJWT = SignedJWT.parse(token);
            // Cria um verificador usando o segredo
            JWSVerifier verifier = new MACVerifier(secret.getBytes());

            // Verifica se a assinatura é válida e se o token não expirou
            if (signedJWT.verify(verifier) && !isTokenExpired(signedJWT)) {
                // Retorna o email (subject) do token
                return signedJWT.getJWTClaimsSet().getSubject();
            } else {
                return null;
            }
        } catch (Exception e) {
            // Qualquer erro retorna null (token inválido ou mal formatado)
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