package com.umc.pureum.domain.sentence;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.pureum.domain.sentence.dto.CreateSentenceReq;
import com.umc.pureum.domain.sentence.dto.CreateSentenceRes;
import com.umc.pureum.domain.sentence.dto.GetBeforeKeywordRes;
import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.domain.sentence.openapi.GetMeansReq;
import com.umc.pureum.domain.sentence.openapi.GetMeansRes;
import com.umc.pureum.domain.sentence.repository.WordRepository;
import com.umc.pureum.domain.user.service.KakaoService;
import com.umc.pureum.domain.user.service.UserService;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import com.umc.pureum.global.utils.JwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;


@RestController
@RequiredArgsConstructor
@Api(tags = "한 문장 챌린지")
@RequestMapping("/sentences")
public class SentenceController {
    private final SentenceProvider sentenceProvider;
    private final SentenceService sentenceService;
    private final JwtService jwtService;
    private final WordRepository wordRepository;
    private final KakaoService kakaoService;
    private final UserService userService;

    /**
     * 한국어 기초 사전 API 연동
     * 한국어 기초 사전 API를 이용하여 단어의 뜻을 받아옴
     * 뜻은 가장 첫 번째로 나오는 뜻을 사용
     * 서버에서 데이터를 입력할 때 사용한 코드로 프론트에는 공개되지 않음
     */
    @ApiIgnore
    @GetMapping("/means")
    public BaseResponse<String> getMeans() {
        String baseUrl = "https://krdict.korean.go.kr/api/search";

        String key = "AF01E186D8067C9F49C4AB17E41B2B39";

        try {
            List<Word> words = wordRepository.findAll();

            for (Word word : words) {
                String now = word.getWord();
                GetMeansReq req = new GetMeansReq(key, now);
                StringBuilder result = new StringBuilder();

                // URL 설정
                URL url = new URL(baseUrl + req.getParameter());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                // 접속
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                BufferedInputStream bufferedInputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream, StandardCharsets.UTF_8));
                String returnLine;
                while ((returnLine = bufferedReader.readLine()) != null) {
                    result.append(returnLine);
                }

                // JSON으로 변환
                JSONObject jsonObject = XML.toJSONObject(result.toString());

                ObjectMapper mapper = new ObjectMapper().configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
                mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

                // GetMeansRes에 매핑
                GetMeansRes getMeansRes = mapper.readValue(jsonObject.toString(), GetMeansRes.class);
                System.out.println(getMeansRes);

                if(getMeansRes.getChannel().getItem().isEmpty()) continue;

                // DB에 뜻 저장
                String meaning = getMeansRes.getChannel().getItem().get(0).getSense().get(0).getDefinition();
                word.setMeaning(meaning);

                wordRepository.save(word);
            }

            return new BaseResponse<>("성공");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return new BaseResponse<>(exception.getMessage());
        }
    }


    /**
     * 오늘의 작성 전 단어 반환 API
     * 작성 전 단어 리스트 반환
     * [GET] /sentences/before
     */
    @ApiOperation("오늘의 작성 전 단어 반환 API")
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

    /**
     * 문장 작성 API
     * [POST] /sentences/write
     */
    @ApiOperation("문장 작성 API")
    @ResponseBody
    @PostMapping("/write")
    public BaseResponse<CreateSentenceRes> writeSentence(@RequestBody CreateSentenceReq request) {

        Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
        String UserId = loggedInUser.getName();

        Long userId = Long.parseLong(UserId);

        try{
            CreateSentenceRes write = sentenceService.write(userId , request);
            return new BaseResponse<>(write);
        }catch (BaseException e){
            return new BaseResponse<>(e.getStatus());
        }

    }



}
