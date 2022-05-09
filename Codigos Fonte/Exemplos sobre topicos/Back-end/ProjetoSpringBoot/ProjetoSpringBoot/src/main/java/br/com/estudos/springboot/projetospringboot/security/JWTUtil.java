package br.com.estudos.springboot.projetospringboot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    @Value("$[jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(String username) {
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS256, secret.getBytes())
            .compact();
    }

    public boolean tokenValido(String token) {
        /*
            Classe do spring responsavel por armazenar as reivindicacoes do usuario
            Ao acessar o end-point o usuario alega possui o email e o tempo de expiracao informados
         */
        Claims claims = getClaims(token);

        if (claims != null) {
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            Date nowDate = new Date(System.currentTimeMillis());
            if (username != null && expirationDate != null && nowDate.before(expirationDate)) {
                return true;
            }
        }
        return false;
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUsername(String token) {
        /*
            Classe do spring responsavel por armazenar as reivindicacoes do usuario
            Ao acessar o end-point o usuario alega possui o email e o tempo de expiracao informados
         */
        Claims claims = getClaims(token);

        if (claims != null) return claims.getSubject();
            return null;
    }
}
