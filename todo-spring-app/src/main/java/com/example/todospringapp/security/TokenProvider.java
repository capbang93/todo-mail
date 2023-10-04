package com.example.todospringapp.security;

import com.example.todospringapp.model.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@Service
public class TokenProvider {
    // HS512로 인코딩하기 위해서는 512바이트보다 커야한다는 에러로 수정
    private static final String SECRET_KEY = "dqdqqweqwewegw3rjkbjkbfjjlfabwjfablwfbjwkfbjwlbfwdqdqqweqwewegw3rjkbjkbfjjlfabwjfablwfbjwkfbjwlbfwdqdqqweqwewegw3rjkbjkbfjjlfabwjfablwfbjwkfbjwlbfwdqdqqweqwewegw3rjkbjkbfjjlfabwjfablwfbjwkfbjwlbfw";

    public String create(UserEntity userEntity) {
        Date expireDate = Date.from(
                Instant.now()
                        .plus(1, ChronoUnit.DAYS)
        );
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .setSubject(userEntity.getId())
                .setIssuer("todo app")
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }
}
