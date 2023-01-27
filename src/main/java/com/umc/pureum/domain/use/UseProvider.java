package com.umc.pureum.domain.use;

import com.umc.pureum.domain.use.dto.GetGoalResultsRes;
import com.umc.pureum.domain.use.dto.GoalResult;
import com.umc.pureum.domain.use.entity.UsePhone;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.domain.user.entity.UserStatus;
import com.umc.pureum.global.config.BaseException;
import com.umc.pureum.global.config.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UseProvider {
    private final UseDao useDao;
    private final UseRepository useRepository;
    private final UserRepository userRepository;

    /** API **/

    /* 목표 달성 여부 반환 API */
    public GetGoalResultsRes getGoalResults(Long userId) throws BaseException {
        // 존재하는 회원인지 검사
        Optional<UserAccount> user = userRepository.findByIdAndStatus(userId, "A");
        if(user.isEmpty()) throw new BaseException(BaseResponseStatus.INVALID_USER);

        // 사용 기록을 받아옴
        List<UsePhone> uses = useRepository.findAllByConditions(userId, getNextDay());

        // 결과 매핑
        GetGoalResultsRes goalResultsRes = new GetGoalResultsRes(userId, uses.stream()
                .map(d -> GoalResult.builder()
                        .date(getYesterday(d.getUpdatedAt()))
                        .isSuccess(getSuccess(d.getUseTime(), d.getPurposeTime())).build())
                .collect(Collectors.toList()));

        return goalResultsRes;
    }

    /* 다음 날 구하기 */
    public Timestamp getNextDay() {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.DATE, 1);

        return new Timestamp(cal.getTime().getTime());
    }

    /* 전 날 구하기 */
    public String getYesterday(Timestamp updated_at) {
        Date date = new Date(updated_at.getTime());
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.DATE, -1);

        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");

        return format.format(cal.getTime());
    }

    /* 날짜 계산 (-9시간) */
    public String getToday(Timestamp createdAt) {
        Date date = new Date(createdAt.getTime());
        Calendar cal = Calendar.getInstance();

        cal.setTime(date);
        cal.add(Calendar.HOUR, -9);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

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
