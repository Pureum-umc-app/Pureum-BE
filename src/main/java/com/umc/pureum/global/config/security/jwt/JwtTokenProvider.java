package com.umc.pureum.global.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;


@Slf4j
@Component
public class JwtTokenProvider {

    public static final String TOKEN_PREFIX = "Bearer ";

    private static final String TOKEN_TYPE = "token_type";
    private static final String ACCESS_TOKEN = "access_token";

    private final SecretKey secretKey;

    private final long accessTokenValidityInMillis;


    private final UserDetailsService userDetailsService;

    private final JwtParser jwtParser;

    //토큰 정보 지정
    public JwtTokenProvider(
            @Value("${spring.security.period}") long accessTokenValidity,
            @Value("${spring.security.key}") String secret,
            UserDetailsService userDetailsService) {
        //토큰 암호화 방식 지정
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        //토큰 유효기간
        this.accessTokenValidityInMillis = accessTokenValidity * 1000 * 60 * 60;
        //Spring Security에서 유저의 정보를 가져오는 인터페이스
        this.userDetailsService = userDetailsService;
        //빌드
        this.jwtParser = Jwts.parserBuilder().setSigningKey(secretKey).build();
    }

    //액세스 토큰 생성
    public String createAccessToken(String username) {
        return createToken(username, ACCESS_TOKEN);
    }

    //토큰 생성
    private String createToken(String username, String tokenType) {
        long expiredTimeMillis = accessTokenValidityInMillis;
        return Jwts.builder()
                .setSubject(username)
                .claim(TOKEN_TYPE, tokenType)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiredTimeMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // 토큰 유효성 및 만료기간 검사
    public boolean validateToken(String token) {
        try {
            jwtParser.parseClaimsJws(token);
            return true;
            //토큰 유효성
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature.");
            log.trace("Invalid JWT signature trace: ", e);
            //토큰 유효기간
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token.");
            log.trace("Expired JWT token trace: ", e);
            //토큰의 형식이 애플리케이션에서 원하는 형식과 맞지 않는 경우
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
            log.trace("Unsupported JWT token trace: ", e);
            //런타임 에러
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
            log.trace("JWT token compact of handler are invalid trace: ", e);
        }
        return false;
    }

    // 토큰에서 인증 정보 추출
    public Authentication getAuthentication(String accessToken) {
        String usernameFromToken = jwtParser.parseClaimsJws(accessToken).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(usernameFromToken);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    //토큰에서 정보 얻기
    public String getSubject(String token) {
        return jwtParser.parseClaimsJws(token).getBody().getSubject();
    }
}