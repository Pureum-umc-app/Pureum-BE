package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.PostUseTimeAndCountReq;
import com.umc.pureum.domain.use.dto.PostUseTimeAndCountRes;
import com.umc.pureum.domain.use.entity.UsePhone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UseService {
    private final UseDao useDao;

    // 일일 사용시간 & 화면 킨 횟수 등록
    @Transactional
    public PostUseTimeAndCountRes saveTimeAndCount(Long user_id,PostUseTimeAndCountReq postUseTimeAndCountReq){
        UsePhone use = useDao.findOneByFk(user_id); // 최근에 생성된 use 테이블 가져오기
        // 최근에 생성된 테이블이 없다면...(목표 설정을 하지 않았다면...)
        if (getDate(use.getCreatedAt()) != getDate(new Timestamp(new Date().getTime()))) {
            UsePhone newUse = UsePhone.builder()
                    .useTime(postUseTimeAndCountReq.getUse_time())
                    .count(postUseTimeAndCountReq.getCount())
                    .createdAt(new Timestamp(new Date().getTime()))
                    .updatedAt(new Timestamp(new Date().getTime()))
                    .build();
            useDao.save(newUse);
        }
        else {
            use.setUseTime(postUseTimeAndCountReq.getUse_time());
            use.setCount(postUseTimeAndCountReq.getCount());
            use.setUpdatedAt(new Timestamp(new Date().getTime()));
        }
        return new PostUseTimeAndCountRes(use.getId());
    }

    /* 날짜 계산 */
    public String getDate(Timestamp created_at) {
        Date date = new Date(created_at.getTime());
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.DATE, 0);

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");

        return format.format(cal.getTime());
    }

}
