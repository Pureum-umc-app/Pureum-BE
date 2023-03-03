package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.request.PostUseTimeAndCountReq;
import com.umc.pureum.domain.use.dto.response.PostUseTimeAndCountRes;
import com.umc.pureum.domain.use.dto.request.ReturnGradeRes;
import com.umc.pureum.domain.use.dto.request.SetUsageTimeReq;
import com.umc.pureum.domain.use.entity.UsePhone;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.umc.pureum.domain.use.entity.UseStatus.A;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UseService {
    private final UserRepository userRepository;
    private final UseRepository useRepository;

    // 일일 사용시간 & 화면 킨 횟수 등록
    @Transactional
    public PostUseTimeAndCountRes saveTimeAndCount(Long userId, PostUseTimeAndCountReq postUseTimeAndCountReq) {
        try {
            UsePhone use = useRepository.findTop1ByUserIdOrderByCreatedAtDesc(userId); // 최근에 생성된 use 테이블 가져오기
            // 입력받은 시간(String -> Time) 변환
            Time useTime = getUseTime(postUseTimeAndCountReq);
            if (getDate(use.getCreatedAt(),0).equals(getDate(new Timestamp(new Date().getTime()),-1))) {
                use.updateUsePhone(useTime, postUseTimeAndCountReq.getCount());
                return new PostUseTimeAndCountRes(use.getId());
            } else { // 최근에 생성된 테이블이 없다면...(목표 설정을 하지 않았다면...)
                UsePhone newUse = getUsePhone(use.getUser(), useTime, postUseTimeAndCountReq);
                return new PostUseTimeAndCountRes(newUse.getId());
            }
        } catch (EmptyResultDataAccessException e){  // 처음 앱을 깔고 목표 설정도 안했다면...
            Time useTime = getUseTime(postUseTimeAndCountReq);
            Optional<UserAccount> user = userRepository.findById(userId);
            UsePhone usePhone = getUsePhone(user.get(), useTime, postUseTimeAndCountReq);
            return new PostUseTimeAndCountRes(usePhone.getId());
        }
    }

    @NotNull
    private UsePhone getUsePhone(UserAccount user, Time useTime, PostUseTimeAndCountReq postUseTimeAndCountReq) {
        UsePhone usePhone = getUsePhone(new UsePhone(user, useTime, postUseTimeAndCountReq.getCount()));
        usePhone.updateCreatedAt(getYesterday());
        return usePhone;
    }

    @NotNull
    private UsePhone getUsePhone(UsePhone user) {
        UsePhone usePhone = user;
        useRepository.save(usePhone);
        return usePhone;
    }

    @NotNull
    private static Time getUseTime(PostUseTimeAndCountReq postUseTimeAndCountReq) {
        Time useTime = Time.valueOf(postUseTimeAndCountReq.getHour() + ":" + postUseTimeAndCountReq.getMinute() + ":0");
        return useTime;
    }

    /* 날짜 계산 */
    public String getDate(Timestamp timestamp, int num) {
        Date date = new Date(timestamp.getTime());
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.DATE, num);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(cal.getTime());
    }

    @Transactional
    public void setUsageTime(Long userId, SetUsageTimeReq setUsageTimeDto) {
        Time useTime = Time.valueOf("0:0:0");
        Time purposeTime = Time.valueOf(setUsageTimeDto.getHour() + ":" + setUsageTimeDto.getMinute() + ":0");
        UserAccount userAccount = userRepository.findByIdAndStatus(userId, "A").orElseThrow();
        getUsePhone(UsePhone.builder()
                .useTime(useTime)
                .user(userAccount)
                .purposeTime(purposeTime)
                .build());
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

    public Timestamp getYesterday() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.DATE, -1);

        return new Timestamp(cal.getTime().getTime());
    }

}
