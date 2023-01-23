package com.umc.pureum.domain.sentence;

import com.umc.pureum.domain.attendance.dto.GetStampRes;
import com.umc.pureum.domain.sentence.dto.GetBeforeKeywordRes;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import com.umc.pureum.global.utils.JwtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
