package com.gustavo.sistemalogin.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    // 1 hora de validade do token
    private static final long EXPIRATION_TIME = 60 * 60 * 1000;

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

    private boolean isTokenExpired(SignedJWT jwt) throws Exception {
        Date expiration = jwt.getJWTClaimsSet().getExpirationTime();
        return expiration == null || expiration.before(new Date());
    }
}