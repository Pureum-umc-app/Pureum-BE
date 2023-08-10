package com.umc.pureum.domain.inquiry;

import com.umc.pureum.domain.inquiry.dto.PostInquiryReq;
import com.umc.pureum.global.config.Response.BaseException;
import com.umc.pureum.global.config.Response.BaseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.umc.pureum.global.config.Response.BaseResponseStatus.DATABASE_ERROR;
import static com.umc.pureum.global.config.Response.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@Api(tags = "문의하기")
@RequestMapping("/contact")
public class InquiryController {

    private final InquiryService inquiryService;
    //private final InquiryRepository inquiryRepository;

    @ApiOperation("문의하기")
    @ApiResponses({
            @ApiResponse(code = 1000, message = "문의가 등록되었습니다.", response = String.class),
            @ApiResponse(code = 2013, message = "이메일을 입력해주세요.")
    })
    @ResponseBody
    @PostMapping()
    public BaseResponse<String> postInquiry(@RequestBody PostInquiryReq postInquiryReq) throws BaseException {
        try{
            inquiryService.saveInquiry(postInquiryReq);
            return new BaseResponse<>(SUCCESS);
        } catch(Exception e){
            e.printStackTrace();
            return new BaseResponse<>(DATABASE_ERROR);
        }
    }

}
