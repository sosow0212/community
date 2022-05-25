package yoon.community.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import yoon.community.entity.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class TokenProvider {
    // 비밀 키
    private static final String SECRET_KEY = "sosow0212";

    // 만료시간
    private static final Date accessTokenExpiresIn = Date.from(Instant.now().plus(1, ChronoUnit.DAYS));

    public String create(User user){

        String accessToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(user.getUsername())
                .setIssuer("spring jwt demo")
                .setIssuedAt(new Date())
                .setExpiration(accessTokenExpiresIn)
                .compact();


        return accessToken;
    }

    public String validateAndSetUserId(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
