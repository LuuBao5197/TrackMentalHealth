package fpt.aptech.trackmentalhealth.ultis;

import fpt.aptech.trackmentalhealth.entities.Users;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

@Component
public class JwtUtils {
    private final String secret = "AIzaSyBFnrG-ATKD6vqtBPyZYXIKfBdEP0I8V2A";

    public String generateToken(String username, Users users) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roleId", users.getRoleId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 giờ
                .signWith(secretToKey(secret))
                .compact();
    }

    private SecretKey secretToKey(String secret) {
        var bytes = secret.getBytes(StandardCharsets.UTF_8);
        try {
            var key = Keys.hmacShaKeyFor(bytes);
            return key;
        } catch (WeakKeyException exception) {
            return Keys.hmacShaKeyFor(Arrays.copyOf(bytes, 64));
        }
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(secretToKey(secret))
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(secretToKey(secret))
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

}