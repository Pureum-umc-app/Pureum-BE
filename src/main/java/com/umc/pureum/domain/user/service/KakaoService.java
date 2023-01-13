package com.umc.pureum.domain.user.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.umc.pureum.domain.user.dto.AccessTokenInfoDto;
import com.umc.pureum.global.config.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {
    public String getToken(String code) throws IOException {
        // 인가코드로 토큰받기
        String host = "https://kauth.kakao.com/oauth/token";
        URL url = new URL(host);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        String token = "";
        String result = null;
        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true); // 데이터 기록 알려주기

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=633bdb4f088357e5fe5cde61b4543053");
            sb.append("&redirect_uri=http://localhost:9000/user/kakao/auth");
            sb.append("&code=" + code);

            bw.write(sb.toString());
            bw.flush();

            int responseCode = urlConnection.getResponseCode();
            System.out.println("responseCode = " + responseCode);

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line = "";
            result = "";
            while ((line = br.readLine()) != null) {
                result += line;
            }
            System.out.println("result = " + result);
            // json parsing
            JSONParser parser = new JSONParser();
            JSONObject elem = (JSONObject) parser.parse(result);
            String access_token = elem.get("access_token").toString();
            String refresh_token = elem.get("refresh_token").toString();
            System.out.println("refresh_token = " + refresh_token);
            System.out.println("access_token = " + access_token);
            token = access_token;
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return token;
    }

    public AccessTokenInfoDto getUserInfoByKakaoToken(String token) throws BaseException {

        String reqURL = "https://kapi.kakao.com/v2/user/me";

        //access_token을 이용하여 사용자 정보 조회
        AccessTokenInfoDto accessTokenInfoDto = null;
        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Authorization", "Bearer " + token); //전송할 header 작성, access_token전송

            //결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();

            //요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";

            while ((line = br.readLine()) != null) {
                result += line;
            }

            //Gson 라이브러리로 JSON파싱
            JsonElement element = JsonParser.parseString(result);
            //accesstoken 정보 Dto에 빌드
            accessTokenInfoDto = AccessTokenInfoDto.builder()
                    .profile_nickname_needs_agreement(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("profile_nickname_needs_agreement").getAsBoolean())
                    .is_email_verified(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("is_email_verified").getAsBoolean())
                    .nickname(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("nickname").getAsString())
                    .email_needs_agreement(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email_needs_agreement").getAsBoolean())
                    .has_email(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("has_email").getAsBoolean())
                    .is_email_valid(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("is_email_valid").getAsBoolean())
                    .profile_image_needs_agreement(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("profile_image_needs_agreement").getAsBoolean())
                    .is_default_image(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("is_default_image").getAsBoolean())
                    .email(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("email").getAsString())
                    .profile_image_url(element.getAsJsonObject().get("kakao_account").getAsJsonObject().get("profile").getAsJsonObject().get("profile_image_url").getAsString())
                    .build();
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return accessTokenInfoDto;
    }
}
