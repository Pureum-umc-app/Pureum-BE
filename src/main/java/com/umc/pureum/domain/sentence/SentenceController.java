package com.umc.pureum.domain.sentence;

import com.umc.pureum.domain.attendance.dto.GetStampRes;
import com.umc.pureum.domain.sentence.dto.GetBeforeKeywordRes;
import com.umc.pureum.domain.sentence.dto.GetMeansReq;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import com.umc.pureum.global.utils.JwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.models.media.XML;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static com.umc.pureum.global.config.BaseResponseStatus.INVALID_JWT;

@RestController
@RequiredArgsConstructor
@Api(tags = "한 문장 챌린지")
@RequestMapping("/sentences")
public class SentenceController {
    private final SentenceProvider sentenceProvider;
    private final SentenceService sentenceService;
    private final JwtService jwtService;

    /**
     * 한국어 기초 사전 API 연동
     * 한국어 기초 사전 API를 이용하여 단어의 뜻을 받아옴
     * 뜻은 가장 첫 번째로 나오는 뜻을 사용
     */
    @ApiIgnore
    @GetMapping("/means")
    public BaseResponse<String> getMeans() {
        String baseUrl = "https://krdict.korean.go.kr/api/search";

        String key = "AF01E186D8067C9F49C4AB17E41B2B39";
        GetMeansReq req = new GetMeansReq(key, "나무");

        try {
            StringBuilder result = new StringBuilder();

            // URL 설정
            URL url = new URL(baseUrl + req.getParameter());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // 접속
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String returnLine;
            result.append("<xmp>");
            while((returnLine = bufferedReader.readLine()) != null) {
                result.append(returnLine).append("\n");
            }

            System.out.println(result);

            return new BaseResponse<>("성공");
        } catch (Exception exception) {
            return new BaseResponse<>(exception.getMessage());
        }
    }


    /**
     * 오늘의 작성 전 단어 반환 API
     * 작성 전 단어 리스트 반환
     * [GET] /sentences/before
     */
    @ApiOperation("도장 개수 반환 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
    @ResponseBody
    @GetMapping("/before")
    public BaseResponse<List<GetBeforeKeywordRes>> getBeforeKeywords() {
        try{
            List<GetBeforeKeywordRes> getBeforeKeywordRes = sentenceProvider.getBeforeKeyword(jwtService.getUserIdx());
            return new BaseResponse<>(getBeforeKeywordRes);
        } catch(BaseException e){
            return new BaseResponse<>(e.getStatus());
        }
    }
}
