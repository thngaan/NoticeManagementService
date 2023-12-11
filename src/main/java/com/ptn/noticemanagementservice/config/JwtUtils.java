package com.ptn.noticemanagementservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtils.class);
    private static final String HEADER_AUTH = "Auth";
    @Value("${jjwt.secret}")
    private String jwtSecret;
    @Value("${jjwt.expiration.ms}")
    private int jwtExpirationMs;
    @Value("${jjwt.cookie-name}")
    private String jwtCookie;

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = validateJwtToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    }

    public ResponseCookie generateJwtCookie(String username) {
        String jwt = generateJwtResponse(username);
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(jwtExpirationMs).httpOnly(true).build();
        return cookie;
    }

    public String generateJwtResponse(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public Claims validateJwtToken(String authToken) {
        return getAllClaimsFromToken(authToken);
    }

    public String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader(HEADER_AUTH);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        } else {
            LOGGER.warn("JWT Token does not begin with Bearer String");
            return null;
        }
    }

    public ResponseCookie getCleanJwtCookie() {
        ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
        return cookie;
    }
}