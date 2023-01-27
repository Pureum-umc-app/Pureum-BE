package com.umc.pureum.domain.mypage;

import com.umc.pureum.domain.mypage.dto.GetMySentencesRes;
import com.umc.pureum.domain.mypage.dto.PostUpdateSentenceReq;
import com.umc.pureum.domain.mypage.dto.reponse.GetProfileResponseDto;
import com.umc.pureum.domain.mypage.dto.request.PatchEditProfileReq;
import com.umc.pureum.domain.user.service.UserService;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.umc.pureum.global.config.BaseResponseStatus.*;

@RestController
@RequiredArgsConstructor
@Api(tags = "마이페이지")
@RequestMapping("/mypages")
public class MyPageController {
    private final MyPageProvider myPageProvider;
    private final MyPageService myPageService;
    private final UserService userService;

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
    public BaseResponse<GetMySentencesRes> getMySentences() throws BaseException {
        try {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String springSecurityUserId = principal.getUsername();
            Long userId = Long.parseLong(springSecurityUserId);
            GetMySentencesRes mySentences = myPageProvider.findMySentences(userId);
            return new BaseResponse<>(mySentences);
        } catch (Exception e) {
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
            @ApiImplicitParam(name = "sentence", paramType = "formData", value = "문장")
    })
    @ResponseBody
    @PutMapping("/sentence/{sentenceId}/edit")
    public BaseResponse<String> UpdateSentence(@PathVariable Long sentenceId, @RequestBody PostUpdateSentenceReq postUpdateSentenceReq) throws BaseException {
        // springSecurity 에서 userId 받아와서 Long 형으로 바꿈
        try {
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String springSecurityUserId = principal.getUsername();
            Long userId = Long.parseLong(springSecurityUserId);
            if (userId != myPageProvider.findSentence(sentenceId).getId()) {
                return new BaseResponse<>(INVALID_USER_JWT);
            } else {
                myPageService.updateSentence(sentenceId, postUpdateSentenceReq);
                return new BaseResponse<>(SUCCESS);
            }
        } catch (Exception e) {
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
    @PutMapping("/sentence/{sentenceId}/delete")
    public BaseResponse<String> UpdateSentence(@PathVariable Long sentenceId) throws BaseException {
        try {
            // springSecurity 에서 userId 받아와서 Long 형으로 바꿈
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String springSecurityUserId = principal.getUsername();
            Long userId = Long.parseLong(springSecurityUserId);
            if (userId != myPageProvider.findSentence(sentenceId).getId()) {
                return new BaseResponse<>(INVALID_USER_JWT);
            } else {
                myPageService.deleteSentence(sentenceId);
                return new BaseResponse<>(SUCCESS);
            }
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @ApiOperation("프로필 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "userId", paramType = "path", value = "유저 인덱스", example = "1")
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다.", response = GetProfileResponseDto.class),
            @ApiResponse(code = 2022, message = "유효하지 않은 JWT입니다.")
    })
    @GetMapping(value = "/{userId}")
    public ResponseEntity<BaseResponse<GetProfileResponseDto>> GetProfile(@PathVariable long userId) throws BaseException {
        try {
            System.out.println(userId);
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            long id = Long.parseLong(principal.getUsername());
            System.out.println(id);
            if (id != userId)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>(INVALID_JWT));
            GetProfileResponseDto getProfileResponseDto = userService.GetProfile(id);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(getProfileResponseDto));
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @ApiOperation("프로필 수정 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "userId", paramType = "path", value = "유저 인덱스", example = "1"),
            @ApiImplicitParam(name = "nickname", paramType = "formData", value = "nickname"),
            @ApiImplicitParam(name = "image", paramType = "formData", value = "image")
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다.", response = GetProfileResponseDto.class),
            @ApiResponse(code = 2022, message = "유효하지 않은 JWT입니다.")
    })
    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<String>> EditProfile(@RequestParam(value = "image", required = false) MultipartFile image, PatchEditProfileReq patchEditProfileReq, @PathVariable long userId) throws BaseException {
        try {
            if (!image.isEmpty()) patchEditProfileReq.setImage(image);
            else patchEditProfileReq.setImage(null);
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            long id = Long.parseLong(principal.getUsername());
            if (id != userId)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>(INVALID_JWT));
            myPageService.EditProfile(patchEditProfileReq, id);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>("정상적으로 수정되었습니다."));
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
