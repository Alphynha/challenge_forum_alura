package br.com.alura.forum_hub.infra.service;

import br.com.alura.forum_hub.domain.usuario.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    @Value("${api.security.secret}")
    private String apiSecret;

    public String gerarToken(Usuario usuario) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("Challenge")
                    .withSubject(usuario.getUsername())
                    .withClaim("id", usuario.getId())
                    .withExpiresAt(TokenExpire())
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            throw new RuntimeException("Falha ao gerar token");
        }
    }

    public String getSubject(String token) {
        if (token == null || token.isBlank()) {
            throw new RuntimeException("Token está vazio");
        }

        DecodedJWT decodedJWT = null;

        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            decodedJWT = JWT.require(algorithm)
                    .withIssuer("Challenge")
                    .build()
                    .verify(token);
            decodedJWT.getSubject();

        } catch (JWTVerificationException e) {
            System.out.println(e.toString());
        }
        if (decodedJWT.getSubject() == null) {
            throw new RuntimeException("Invalid token");
        }
        return decodedJWT.getSubject();
    }

    private Instant TokenExpire() {
        return LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.of( "-03:00"));
    }
}
