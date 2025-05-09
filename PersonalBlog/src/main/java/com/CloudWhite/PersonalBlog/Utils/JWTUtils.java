package com.CloudWhite.PersonalBlog.Utils;

import com.CloudWhite.PersonalBlog.Entity.DTO.token;
import com.CloudWhite.PersonalBlog.Entity.role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtils {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static String createAccessToken(token token, long expiration) {
        return Jwts.builder()
                .setSubject(String.valueOf(token.getUserId()))
                .claim("username", token.getUsername())
                .claim("userId", token.getUserId())
                .claim("roleName", token.getRole().getRoleName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public static String createRefreshToken(token token, long expiration) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(String.valueOf(token.getUserId()))
                .claim("username", token.getUsername())
                .claim("userId", String.valueOf(token.getUserId()))
                .claim("roleName", token.getRole().getRoleName())
                .setIssuedAt(new Date());
        if (expiration > 0) {
            builder.setExpiration(new Date(System.currentTimeMillis() + expiration));
        }
        return builder.signWith(SECRET_KEY, SignatureAlgorithm.HS512).compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public static boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token); // 解析 token
            Date expiration = claims.getExpiration();
            return !expiration.before(new Date());  // 判断是否过期
        } catch (ExpiredJwtException e) {// token 已过期
            return false;
        } catch (SignatureException e) {// token 签名无效
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String refreshAccessToken(String refreshToken, long accessTokenExpiration) {
        Claims claims = parseToken(refreshToken);

        token newToken = new token();
        role newRole = new role();
        newRole.setRoleName(claims.get("roleName", String.class));
        newToken.setRole(newRole);
        newToken.setUsername(claims.get("username", String.class));
        newToken.setUserId(claims.get("userId", Integer.class));

        return createAccessToken(newToken, accessTokenExpiration);
    }
}
