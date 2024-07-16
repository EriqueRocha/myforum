package dev.erique.myforum.infra.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.erique.myforum.infra.handler.ResponseFactory;
import dev.erique.myforum.model.admin.Admin;
import dev.erique.myforum.model.user.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private static final String MESSAGE = "auth-api";
    private static final String MESSAGE2 = "erro com a geração de token";

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(Client client){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(MESSAGE)
                    .withSubject(client.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        }catch (JWTCreationException exception){
            throw new SecurityException(MESSAGE2, exception);
        }
    }

    public String generateToken(Admin admin){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(MESSAGE)
                    .withSubject(admin.getEmail())
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);
        }catch (JWTCreationException exception){
            throw new SecurityException(MESSAGE2, exception);
        }
    }

    public String validadeToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer(MESSAGE)
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException exception){
            return "Token invalido ou expirado";
        }
    }
    private Instant genExpirationDate(){
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubject(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token.replace("Bearer ", ""));
            return jwt.getSubject();
        }catch (JWTVerificationException exception){
            return ResponseFactory.errorUnautorized("Token invalido ou expirado", exception.getMessage()).toString();
        }
    }
}