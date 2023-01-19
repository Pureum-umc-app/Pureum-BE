package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.GetUseTimeAndCountRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UseProvider {
    private final UseDao useDao;

    // 일일 사용시간 & 화면 킨 횟수 조회(회원가입부터 지금까지)
    public List<GetUseTimeAndCountRes> getTimeAndCount(Long user_id){

    }
}
