package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.PostUseTimeAndCountReq;
import com.umc.pureum.domain.use.dto.PostUseTimeAndCountRes;
import com.umc.pureum.domain.use.entity.Use;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UseService {
    private final UseDao useDao;

    // 일일 사용시간 & 화면 킨 횟수 등록
    @Transactional
    public PostUseTimeAndCountRes saveTimeAndCount(PostUseTimeAndCountReq postUseTimeAndCountReq){
        Long user_id = postUseTimeAndCountReq.getUser_id();
        Use use = useDao.findOne(user_id);
        use.builder().use_time(use.getUse_time()).count(use.getCount());
        return new PostUseTimeAndCountRes(use.getId());
    }
}
