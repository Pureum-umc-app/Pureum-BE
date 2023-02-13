package com.umc.pureum.domain.mypage;

import com.umc.pureum.domain.mypage.dto.response.GetMySentencesRes;
import com.umc.pureum.domain.mypage.dto.request.PostUpdateSentenceReq;
import com.umc.pureum.domain.mypage.dto.response.GetProfileResponseDto;
import com.umc.pureum.domain.mypage.dto.request.PatchEditProfileReq;
import com.umc.pureum.domain.user.service.UserService;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import static com.umc.pureum.global.config.BaseResponseStatus.*;
import static com.umc.pureum.global.utils.FileCheck.*;

@Slf4j
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
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
    })
    @ResponseBody
    @GetMapping("/sentence")
    public BaseResponse<GetMySentencesRes> getMySentences() {
        // springSecurity 에서 userId 받아와서 Long 형으로 바꿈
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String springSecurityUserId = principal.getUsername();
        Long userId = Long.parseLong(springSecurityUserId);
        GetMySentencesRes mySentences = myPageProvider.findMySentences(userId);
        return new BaseResponse<>(mySentences);
    }

    /**
     * 문장 수정 API
     * [PATCH] /mypages/sentence/{sentenceId}/edit
     */
    @ApiOperation("문장 수정")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "sentenceId", paramType = "path", value = "문장 인덱스", example = "1", dataTypeClass = Long.class),
            @ApiImplicitParam(name = "postUpdateSentenceReq", paramType = "body", value = "문장", dataTypeClass = PostUpdateSentenceReq.class)
    })
    @ResponseBody
    @PatchMapping("/sentence/{sentenceId}/edit")
    public BaseResponse<String> UpdateSentence(@PathVariable Long sentenceId, @RequestBody PostUpdateSentenceReq postUpdateSentenceReq) {
        try {
            // springSecurity 에서 userId 받아와서 Long 형으로 바꿈
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String springSecurityUserId = principal.getUsername();
            Long userId = Long.parseLong(springSecurityUserId);
            if (userId != myPageProvider.findSentence(sentenceId).getUser().getId()) {
                return new BaseResponse<>(INVALID_USER_JWT);
            } else {
                if (postUpdateSentenceReq.getSentence().isEmpty()) { // 공백이면 에러 메세지 출력
                    return new BaseResponse<>(POST_SENTENCE_EMPTY);
                } else if (!postUpdateSentenceReq.getSentence().contains(myPageService.getKeyword(sentenceId))) { // 키워드 포함 안하면 에러 메세지 출력
                    return new BaseResponse<>(POST_SENTENCE_NO_EXISTS_KEYWORD);
                } else {
                    myPageService.updateSentence(sentenceId, postUpdateSentenceReq);
                    return new BaseResponse<>(SUCCESS);
                }
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    /**
     * 문장 삭제 API
     * [PUT] /mypages/sentence/{sentenceId}/delete
     */
    @ApiOperation("문장 삭제")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "sentenceId", paramType = "path", value = "문장 인덱스", example = "1", dataTypeClass = Long.class)
    })
    @ResponseBody
    @PatchMapping("/sentence/{sentenceId}/delete")
    public BaseResponse<String> UpdateSentence(@PathVariable Long sentenceId) {
        try {
            // springSecurity 에서 userId 받아와서 Long 형으로 바꿈
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String springSecurityUserId = principal.getUsername();
            Long userId = Long.parseLong(springSecurityUserId);
            if (userId != myPageProvider.findSentence(sentenceId).getUser().getId()) {
                return new BaseResponse<>(INVALID_USER_JWT);
            } else {
                myPageService.deleteSentence(sentenceId);
                return new BaseResponse<>(SUCCESS);
            }
        } catch (BaseException e) {
            return new BaseResponse<>(e.getStatus());
        }
    }

    @ApiOperation("프로필 조회 API")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", dataTypeClass = String.class, paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "userId", dataTypeClass = Long.class, paramType = "path", value = "유저 인덱스", example = "1")
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
            @ApiImplicitParam(name = "Authorization", dataTypeClass = String.class, paramType = "header", value = "서비스 자체 jwt 토큰"),
            @ApiImplicitParam(name = "userId", dataTypeClass = Long.class, paramType = "path", value = "유저 인덱스", example = "1"),
            @ApiImplicitParam(name = "nickname", dataTypeClass = String.class, paramType = "formData", value = "nickname"),
            @ApiImplicitParam(name = "image", dataTypeClass = MultipartFile.class, paramType = "formData", value = "image")
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "요청에 성공하였습니다.", response = GetProfileResponseDto.class),
            @ApiResponse(code = 2022, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2005, message = "이미지파일이 아닙니다")
    })
    @PatchMapping(value = "/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BaseResponse<String>> EditProfile(@RequestParam(value = "image", required = false) MultipartFile image, PatchEditProfileReq patchEditProfileReq, @PathVariable long userId) throws BaseException {
        try {
            if (!image.isEmpty()) {
                if (!checkImage(image))
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>(INVALID_IMAGE_FILE));
                patchEditProfileReq.setImage(image);
            } else patchEditProfileReq.setImage(null);
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            long id = Long.parseLong(principal.getUsername());
            if (id != userId)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseResponse<>(INVALID_JWT));

            System.out.println(patchEditProfileReq);
            myPageService.EditProfile(patchEditProfileReq, id);
            return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>("정상적으로 수정되었습니다."));
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
