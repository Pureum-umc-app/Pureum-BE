package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.GetUseTimeAndCountRes;
import com.umc.pureum.domain.use.dto.PostUseTimeAndCountReq;
import com.umc.pureum.domain.use.dto.PostUseTimeAndCountRes;
import com.umc.pureum.domain.use.entity.Use;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UseService {
    private final UseDao useDao;

    // 일일 사용시간 & 화면 킨 횟수 등록
    @Transactional
    public PostUseTimeAndCountRes saveTimeAndCount(Long user_id,PostUseTimeAndCountReq postUseTimeAndCountReq){
        Use use = useDao.findOneByFk(user_id);
        use.setUse_time(postUseTimeAndCountReq.getUse_time());
        use.setCount(postUseTimeAndCountReq.getCount());
        use.setUpdated_at(LocalDateTime.now());
        return new PostUseTimeAndCountRes(use.getId());
    }

}
