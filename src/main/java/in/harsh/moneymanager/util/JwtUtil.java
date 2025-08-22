package in.harsh.moneymanager.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    public String secret;

    @Value("${jwt.expiration}")
    public long expiration;

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)  // subject = email
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration ))
                .signWith(SignatureAlgorithm.HS256, secret )
                .compact();
    }
}
