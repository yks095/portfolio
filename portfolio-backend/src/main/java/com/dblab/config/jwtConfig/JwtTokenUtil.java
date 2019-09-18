package com.dblab.config.jwtConfig;

import com.dblab.domain.User;
import com.dblab.service.CustomUserDetailsService;
import com.dblab.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    //3시간 후 만료
    private static final long JWT_TOKEN_VALIDITY = 3 * 1000 * 60 * 60;

    @Value("${jwt.secret}")
    private String JWT_SECRET_KEY;

    //토큰 생성
    public String generateToken(User user){
        Map<String, Object> claims = new HashMap<>();

        claims.put("idx", user.getIdx());
        claims.put("email", user.getEmail());

        return doGenerateToken(claims, user.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                    .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY)
                    .compact();
    }

    public String usernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String jwtToken, UserDetails userDetails) {
        String username = usernameFromToken(jwtToken);

        return username.equals(userDetails.getUsername()) && isTokenExpired(jwtToken);
    }


    private boolean isTokenExpired(String jwtToken) {
        Date period = getExpirationFromToken(jwtToken);
        return period.after(new Date());
    }

    private Date getExpirationFromToken(String jwtToken) {
        return getClaimFromToken(jwtToken, Claims::getExpiration);

    }
}
