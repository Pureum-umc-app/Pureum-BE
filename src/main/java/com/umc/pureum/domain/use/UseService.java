package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.request.PostUseTimeAndCountReq;
import com.umc.pureum.domain.use.dto.response.PostUseTimeAndCountRes;
import com.umc.pureum.domain.use.dto.request.ReturnGradeRes;
import com.umc.pureum.domain.use.dto.request.SetUsageTimeReq;
import com.umc.pureum.domain.use.entity.UsePhone;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UseService {
    private final UseDao useDao;
    private final UserRepository userRepository;
    private final UseRepository useRepository;

    // 일일 사용시간 & 화면 킨 횟수 등록
    @Transactional
    public PostUseTimeAndCountRes saveTimeAndCount(Long userId, PostUseTimeAndCountReq postUseTimeAndCountReq) {
        UsePhone use = useDao.findOneByFk(userId); // 최근에 생성된 use 테이블 가져오기
        // 입력받은 시간(String -> Time) 변환
        Time useTime = Time.valueOf(postUseTimeAndCountReq.getHour() + ":" + postUseTimeAndCountReq.getMinute() + ":0");
       if (getDate(use.getCreatedAt()).equals(getDate(new Timestamp(new Date().getTime())))) {
           use.updateUsePhone(useTime, postUseTimeAndCountReq.getCount());
           return new PostUseTimeAndCountRes(use.getId());
        } else { // 최근에 생성된 테이블이 없다면...(목표 설정을 하지 않았다면...)
           UsePhone newUse = new UsePhone(use.getUser(), useTime, postUseTimeAndCountReq.getCount());
           useDao.save(newUse);
           return new PostUseTimeAndCountRes(newUse.getId());
        }
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

    @Transactional
    public void setUsageTime(Long userId, SetUsageTimeReq setUsageTimeDto) {
        Time useTime = Time.valueOf("0:0:0");
        Time purposeTime = Time.valueOf(setUsageTimeDto.getHour() + ":" + setUsageTimeDto.getMinute() + ":0");
        UserAccount userAccount = userRepository.findByIdAndStatus(userId, "A").orElseThrow();
        UsePhone usePhone = UsePhone.builder()
                .useTime(useTime)
                .user(userAccount)
                .purposeTime(purposeTime)
                .build();
        useRepository.save(usePhone);
    }

    public boolean existUsageTime(Long userId) {
        if (useRepository.existUsageTime(userId, Pageable.ofSize(1)).size()==0)
            return false;
        else
            return true;
    }

    public ReturnGradeRes returnGrade(Long userId) {

        // request 로 받은 userId 로 userAccount 찾기
        UserAccount userAccount = userRepository.findById(userId).get();

        // userAccount 에서 해당 user 의 grade 찾기
        int grade = userAccount.getGrade();

        return new ReturnGradeRes(userId , grade);
    }
}
