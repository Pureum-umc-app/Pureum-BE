package com.umc.pureum.domain.blame;

import com.umc.pureum.global.config.Response.BaseException;
import com.umc.pureum.global.config.Response.BaseResponse;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/blame")
public class BlameController {
    private final BlameService blameService;

    @ApiOperation("배틀 문장 신고")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "battleSentenceId", dataTypeClass = Long.class, paramType = "path", value = "battleSentenceId"),
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "신고 되었습니다. or 신고 취소 되었습니다.", response = String.class),
            @ApiResponse(code = 2022, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2034, message = "존재하지 않는 회원입니다.")
    })
    @PostMapping(value = "/battle-sentence/{battleSentenceId}")
    public ResponseEntity<BaseResponse<String>> battleSentenceBlame(@PathVariable long battleSentenceId) {
        try {
            Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
            long userId = Long.parseLong(loggedInUser.getName());
            if (blameService.battleSentenceBlame(userId, battleSentenceId)) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>("신고 되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>("신고 취소 되었습니다."));
            }
        } catch (BaseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(e.getStatus()));
        }
    }

    @ApiOperation("오늘의 문장 신고")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", paramType = "header", value = "서비스 자체 jwt 토큰", dataTypeClass = String.class),
            @ApiImplicitParam(name = "sentenceId", dataTypeClass = Long.class, paramType = "path", value = "sentenceId"),
    })
    @ApiResponses({
            @ApiResponse(code = 1000, message = "신고 되었습니다. or 신고 취소 되었습니다.", response = String.class),
            @ApiResponse(code = 2022, message = "유효하지 않은 JWT입니다."),
            @ApiResponse(code = 2034, message = "존재하지 않는 회원입니다.")
    })
    @PostMapping(value = "/sentence/{sentenceId}")
    public ResponseEntity<BaseResponse<String>> sentenceBlame(@PathVariable long sentenceId) {
        try {
            Authentication loggedInUser = SecurityContextHolder.getContext().getAuthentication();
            long userId = Long.parseLong(loggedInUser.getName());
            if (blameService.sentenceBlame(userId, sentenceId)) {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>("신고 되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>("신고 취소 되었습니다."));
            }
        } catch (BaseException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse<>(e.getStatus()));
        }
    }
}
