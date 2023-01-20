package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.PostUseTimeAndCountReq;
import com.umc.pureum.domain.use.dto.PostUseTimeAndCountRes;
import com.umc.pureum.domain.use.entity.UsePhone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UseService {
    private final UseDao useDao;

    // 일일 사용시간 & 화면 킨 횟수 등록
    @Transactional
    public PostUseTimeAndCountRes saveTimeAndCount(Long user_id,PostUseTimeAndCountReq postUseTimeAndCountReq){
        UsePhone use = useDao.findOneByFk(user_id);
        use.setUseTime(postUseTimeAndCountReq.getUse_time());
        use.setCount(postUseTimeAndCountReq.getCount());
        use.setUpdatedAt(new Timestamp(new Date().getTime()));
        return new PostUseTimeAndCountRes(use.getId());
    }
}
