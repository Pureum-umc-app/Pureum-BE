package com.umc.pureum.domain.mypage;

import com.umc.pureum.domain.mypage.dto.GetMySentencesRes;
import com.umc.pureum.domain.mypage.dto.PostUpdateSentenceReq;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import static com.umc.pureum.global.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "마이페이지")
@RequestMapping("/mypages")
public class MyPageController {
    private final MyPageProvider myPageProvider;
    private final MyPageService myPageService;

    /**
     * 나의 문장 리스트 반환 API
     * [GET] /mypages/sentence
     */
    @ApiOperation("나의 문장 리스트 반환 ")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
    })
    @ResponseBody
    @GetMapping("/sentence")
    public BaseResponse<GetMySentencesRes> getMySentences() throws BaseException{
        try{
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String springSecurityUserId = principal.getUsername();
            Long userId = Long.parseLong(springSecurityUserId);
            GetMySentencesRes mySentences = myPageProvider.findMySentences(userId);
            return new BaseResponse<>(mySentences);
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    
    /**
     * 문장 수정 API
     * [POST] /mypages/sentence/{sentenceId}/edit
     */
    @ApiOperation("문장 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header"),
            @ApiImplicitParam(name = "sentence",paramType = "formData",value = "문장")
    })
    @ResponseBody
    @PostMapping("/sentence/{sentenceId}/edit")
    public BaseResponse<String> UpdateSentence(@PathVariable Long sentenceId, @RequestBody PostUpdateSentenceReq postUpdateSentenceReq) throws BaseException {
        // springSecurity 에서 userId 받아와서 Long 형으로 바꿈
        try{
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String springSecurityUserId = principal.getUsername();
            Long userId = Long.parseLong(springSecurityUserId);
            if(userId != myPageProvider.findSentence(sentenceId).getId()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            else {
                myPageService.updateSentence(sentenceId, postUpdateSentenceReq);
                return new BaseResponse<>(SUCCESS);
            }
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    /**
     * 문장 삭제 API
     * [POST] /mypages/sentence/{sentenceId}/delete
     */
    @ApiOperation("문장 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-ACCESS-TOKEN", required = true, dataType = "string", paramType = "header")
    })
    @ResponseBody
    @PostMapping("/sentence/{sentenceId}/delete")
    public BaseResponse<String> UpdateSentence(@PathVariable Long sentenceId) throws BaseException{
        try{
            // springSecurity 에서 userId 받아와서 Long 형으로 바꿈
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String springSecurityUserId = principal.getUsername();
            Long userId = Long.parseLong(springSecurityUserId);
            if(userId != myPageProvider.findSentence(sentenceId).getId()){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            else {
                myPageService.deleteSentence(sentenceId);
                return new BaseResponse<>(SUCCESS);
            }
        }catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
