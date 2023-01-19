package com.umc.pureum.global.utils;

import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.secret.Secret;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.umc.pureum.global.config.BaseResponseStatus.EMPTY_JWT;
import static com.umc.pureum.global.config.BaseResponseStatus.INVALID_JWT;

@Service
public class JwtService {
    private static final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 60;  // 2달
    private final long REFRESH_TOKEN_VALID_TIME = 1000L * 60 * 60 * 24 * 365;  // 1년

    /* Access Token 생성 */
    public static String createAccessToken(int userIdx) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx",userIdx)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_ACCESS_TOKEN_KEY)
                .compact();
    }

    /* Header에서 X-ACCESS-TOKEN 으로 JWT 추출 */
    public String getJwt(){
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("X-ACCESS-TOKEN");
    }

    /* JWT에서 userIdx 추출 */
    public Long getUserIdx() throws BaseException {
        // 1. JWT 추출
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_ACCESS_TOKEN_KEY)
                    .parseClaimsJws(accessToken);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }

//        // 3. 유효 기간 확인
//        Date exp = claims.getBody().getExpiration();
//        System.out.println(exp);

        // 3. userIdx 추출
        return claims.getBody().get("userIdx", Long.class);  // jwt 에서 userIdx를 추출합니다.
    }

    /* 이 부분은 개인적으로 refresh token 공부하면서 추가했던 부분인데 아직은 신경 안 쓰셔도 될 거 같아요..!
       일단 혹시 몰라서 남겨둡니다!
     */

    /* Refresh Token 생성 */
    public String createRefreshToken(int userIdx) {
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("userIdx",userIdx)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, Secret.JWT_REFRESH_TOKEN_KEY)
                .compact();
    }

    /* JWT 유효기한 지났는지 확인 */
    public boolean checkValid(String jwt) throws BaseException {
        // 1. JWT 추출
        String accessToken = getJwt();
        if(accessToken == null || accessToken.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(Secret.JWT_ACCESS_TOKEN_KEY)
                    .parseClaimsJws(jwt);

            Date now = new Date();
            return now.after(claims.getBody().getExpiration());
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }
    }

}
