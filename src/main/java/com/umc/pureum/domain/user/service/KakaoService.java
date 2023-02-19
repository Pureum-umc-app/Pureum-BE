package com.umc.pureum.domain.user.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.umc.pureum.domain.user.dto.request.KakaoAccessTokenInfoDto;
import com.umc.pureum.global.config.Response.BaseException;
import com.umc.pureum.global.config.Response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {
    /**
     * 인가코드로 토큰 받기
     *
     * @param code 인가코드
     * @throws IOException // 카카오서버 접속 오류
     */
    @Value("${kakao.client_id}")
    private String client_id;
    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    public void getToken(String code) throws IOException {
        // 인가코드로 토큰받기
        String host = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        StringBuilder result;
        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            String sb = "grant_type=authorization_code" +
                    "&client_id=" + client_id +
                    "&redirect_uri=" + redirect_uri +
                    "&code=" + code;

            bw.write(sb);
            bw.flush();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            System.out.println("result = " + result);
            // json parsing
            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(result.toString());
            String access_token = elem.get("access_token").toString();
            String refresh_token = elem.get("refresh_token").toString();
            System.out.println("refresh_token = " + refresh_token);
            System.out.println("access_token = " + access_token);
            br.close();
            bw.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * access token으로 유저 정보 가져오기
     *
     * @param token //access token
     * @return // 유저 정보 AccessTokenInfoDto 형태로 리턴
     */
    public KakaoAccessTokenInfoDto getUserInfoByKakaoToken(String token) throws BaseException, IOException {

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        KakaoAccessTokenInfoDto kakaoAccessTokenInfoDto = null;

        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

        //결과 코드가 200이라면 성공
        int responseCode = conn.getResponseCode();
        if (responseCode == 401) {
            throw new BaseException(BaseResponseStatus.INVALID_KAKAO_TOKEN);
        }
        //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();

        while ((line = br.readLine()) != null) {
            result.append(line);
        }

        //Gson 라이브러리로 JSON파싱
        JsonElement element = JsonParser.parseString(result.toString());
        //accesstoken 정보 Dto에 빌드
        log.info(element.toString());
        if (!element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean()) {
            kakaoAccessTokenInfoDto = KakaoAccessTokenInfoDto.builder()
                    .id(element.getAsJsonObject().get("id").getAsLong())
                    .is_email_verified(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("is_email_verified").getAsBoolean())
                    .email_needs_agreement(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email_needs_agreement").getAsBoolean())
                    .has_email(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean())
                    .is_email_valid(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("is_email_valid").getAsBoolean())
                    .email(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString())
                    .build();
        } else {
            kakaoAccessTokenInfoDto = KakaoAccessTokenInfoDto.builder()
                    .id(element.getAsJsonObject().get("id").getAsLong())
                    .email_needs_agreement(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email_needs_agreement").getAsBoolean())
                    .has_email(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean())
                    .build();
        }
        br.close();
        //access token으로 받은 유저정보 return
        return kakaoAccessTokenInfoDto;
    }
}