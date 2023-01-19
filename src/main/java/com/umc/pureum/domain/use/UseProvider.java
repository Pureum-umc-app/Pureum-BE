package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.GetGoalResultsRes;
import com.umc.pureum.domain.use.entity.UsePhone;
import com.umc.pureum.domain.use.entity.UseStatus;
import com.umc.pureum.domain.user.entity.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UseProvider {
    private final UseDao useDao;
    private final UseRepository useRepository;

    /** API **/

    /* 목표 달성 여부 반환 API */
    public List<GetGoalResultsRes> getGoalResults(Long userIdx) {
        Date now = new Date();
        Timestamp timestamp = new Timestamp(now.getTime());

        List<UsePhone> uses = useRepository.findAllByConditions(userIdx, getNextDay());

        return uses.stream()
                .map(d -> GetGoalResultsRes.builder()
                        .date(getDate(d.getUpdatedAt()))
                        .isSuccess(getSuccess(d.getUseTime(), d.getPurposeTime())).build())
                .collect(Collectors.toList());
    }

    /* 다음 날 구하기 */
    public Timestamp getNextDay() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.DATE, 1);

        return new Timestamp(cal.getTime().getTime());
    }

    /* 날짜 계산 */
    public String getDate(Timestamp updated_at) {
        Date date = new Date(updated_at.getTime());
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.DATE, -1);

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");

        return format.format(cal.getTime());
    }

    /* 성공 여부 계산 */
    public int getSuccess(Time use_time, Time purpose_time) {
        if(use_time.after(purpose_time)) {
            return 0;  // 실패
        } else {
            return 1;  // 성공
        }
    }


    /** 유효성 검사 **/
}
