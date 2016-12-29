package com.concrete.solution.java.test.service;

import com.concrete.solution.java.test.exception.UserNotAuthorizedException;
import com.concrete.solution.java.test.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenService implements Serializable {

    private static final long serialVersionUID = -3301605591108950415L;

    static final String CLAIM_KEY_ID = "id";
    static final String CLAIM_KEY_CREATED = "created";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public Integer getIdFromToken(String token) {
        Integer id;
        try {
            final Claims claims = getClaimsFromToken(token);
            id = (Integer) claims.get(CLAIM_KEY_ID);
        } catch (Exception e) {
            id = null;
        }
        return id;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            created = null;
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            expiration = claims.getExpiration();
        } catch (Exception e) {
            expiration = null;
        }
        return expiration;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean isThirtyMinLessLastLogin(Date lastLogin) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, -30);
        Date thirtyMinBack = cal.getTime();
        return (lastLogin != null && thirtyMinBack.before(lastLogin));
    }


    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_ID, user.getId());
        claims.put(CLAIM_KEY_CREATED, user.getCreated());
        return generateToken(claims);
    }

    String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Boolean validateToken(String token, User user) {

        final Integer id = getIdFromToken(token);
        final Date created = getCreatedDateFromToken(token);

        if(!id.equals(user.getId()) && !token.equals(user.getToken()))
            throw new UserNotAuthorizedException();

        if(!isThirtyMinLessLastLogin(user.getLastLogin()))
            throw new UserNotAuthorizedException();

        return true;
    }
}
