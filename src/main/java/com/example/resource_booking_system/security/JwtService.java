package com.example.resource_booking_system.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtService {
    private static String secretkey;
    JwtService(){
        SecureRandom random=new SecureRandom();
        byte[] key=new byte[32];
        random.nextBytes(key);
        secretkey= Base64.getEncoder().encodeToString(key);
    }
    public String generateToken(String username, List<String> roles){
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+1000*60*20))
                .signWith(getSignedKey())
                .compact();
    }
    private Key getSignedKey(){
        byte[] keyBytes= Decoders.BASE64.decode(secretkey);
        return Keys.hmacShaKeyFor(keyBytes);

    }

    public Boolean validToken(String token, String username){
        return (extractUsername(token).equals(username) && !isTokenExpired(token));
    }
    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);

    }
    public Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
    public Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());

    }
    public List<String> extractRole(String token){
        return extractClaim(token, claims ->claims.get("roles",List.class));
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims= Jwts.parser()
                .verifyWith((SecretKey) getSignedKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claimsResolver.apply(claims);
    }
}