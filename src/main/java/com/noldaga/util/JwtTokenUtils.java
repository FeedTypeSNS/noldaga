package com.noldaga.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtTokenUtils {

    public static String generateToken(String username,String key, long expiredTimeMs ) {
        Claims claims = Jwts.claims();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims) //사용자 정보
                .setIssuedAt(new Date(System.currentTimeMillis())) //토큰만료정보
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getKey(key), SignatureAlgorithm.HS256) //서버거 토큰을 검증할때 사용될 정보
                .compact();
    }


    private static Key getKey(String key){
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    //이 아래는 필터 에서 사용 되는 메서드

    public static String extractUsername(String token, String key){
        Claims claims = extractClaims(token, key);
        return claims.get("username", String.class); //토큰 만들때 "username" 에 넣어줬던것을 반환
    }

    public static boolean isExpired(String token,String key){
        Claims claims = extractClaims(token, key);
        Date expiredDate = claims.getExpiration();
        return expiredDate.before(new Date());//new Date() 는 현재시각
    }

    private static Claims extractClaims(String token, String key){
        return Jwts.parserBuilder().setSigningKey(getKey(key))
                .build().parseClaimsJws(token).getBody();
    }


}
