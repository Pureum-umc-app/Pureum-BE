package com.umc.pureum.domain.test;

import com.umc.pureum.global.config.Response.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/test")
public class TestController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    public TestController() {
    }

    /**
     * 로그 테스트 API
     * [GET] /test/log
     *
     * @return String
     */
    @ResponseBody
    @GetMapping("/log")
    public ResponseEntity<BaseResponse<String>> getAll() {
        System.out.println("테스트");
//        trace, debug 레벨은 Console X, 파일 로깅 X
//        logger.trace("TRACE Level 테스트");
//        logger.debug("DEBUG Level 테스트");

//        info 레벨은 Console 로깅 O, 파일 로깅 X
        logger.info("INFO Level 테스트");
//        warn 레벨은 Console 로깅 O, 파일 로깅 O
        logger.warn("Warn Level 테스트");
//        error 레벨은 Console 로깅 O, 파일 로깅 O (app.log 뿐만 아니라 error.log 에도 로깅 됨)
//        app.log 와 error.log 는 날짜가 바뀌면 자동으로 *.gz 으로 압축 백업됨
        logger.error("ERROR Level 테스트");

//        System.out.println(sentenceLikeRepository.findByStatus( "A", PageRequest.of(0,1,Sort.by(Sort.Order.desc("id")))).getContent());
//        sentenceLikeMappings.forEach(System.out::println);
        LocalDateTime now = LocalDateTime.now();
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(now.toString()));
    }
}
