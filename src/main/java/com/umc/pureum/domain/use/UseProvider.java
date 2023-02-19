package com.umc.pureum.domain.use;

import com.umc.pureum.domain.use.dto.time.TimeInfo;
import com.umc.pureum.domain.use.dto.response.GetGoalResultsRes;
import com.umc.pureum.domain.use.dto.response.GetHomeListRes;
import com.umc.pureum.domain.use.dto.response.GoalResult;
import com.umc.pureum.domain.use.dto.response.RankerInformationDto;
import com.umc.pureum.domain.use.entity.UsePhone;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.config.Response.BaseException;
import com.umc.pureum.global.config.Response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UseProvider {

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

        return new GetGoalResultsRes(userId, uses.stream()
                .map(d -> GoalResult.builder()
                        .date(getYesterday(d.getUpdatedAt()))
                        .isSuccess(getSuccess(d.getUseTime(), d.getPurposeTime())).build())
                .collect(Collectors.toList()));
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(cal.getTime());
    }

    /* 날짜 계산 */
    public String getToday(Timestamp createdAt) {
        Date date = new Date(createdAt.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        return format.format(date);
    }

    /* 성공 여부 계산 */
    public Integer getSuccess(Time use_time, Time purpose_time) {
        if(use_time == null || purpose_time == null) {
            return null;
        }

        if(use_time.after(purpose_time)) {
            return 0;  // 실패
        } else {
            return 1;  // 성공
        }
    }

    // 홈 화면 리스트 반환
    public List<GetHomeListRes> getHomeListRes(Long userId){
        List<UsePhone> useAll = useRepository.findByUserId(userId);
        return useAll.stream().map(u -> new GetHomeListRes(stringToIntForDate(getYesterday(u.getUpdatedAt())),
                        stringToIntForTime(preventNullError(u.getUseTime())),
                        stringToIntForTime(preventNullError(u.getPurposeTime())),
                        u.getCount(),
                        getRankerInformation(u.getUpdatedAt(), u.getUser().getGrade())))
                .collect(Collectors.toList());
    }

    // 랭킹 Top 10 사용자 정보 조회(같은 카테고리(학년) 내)
    public List<RankerInformationDto> getRankerInformation(Timestamp updatedAt, int grade){
        List<UsePhone> rankTopTen = useRepository.findRankTenInSameGrade(updatedAt, grade);
        return moveUsePhoneToRankerInfo(rankTopTen);
    }

    // 날짜 별 랭킹 전체 조회(같은 카테고리(학년) 내)
    public List<RankerInformationDto> getRankerInformationByDateInSameGrade(Long userId, String date, int page){
        Timestamp getDate = getTimeStampFromString(date);
        int grade = userRepository.findById(userId).get().getGrade();
        Slice<UsePhone> rankInSameGrade = useRepository.findRankInSameGrade(getDate, grade, PageRequest.of(page * 25, 25));
        return moveUsePhoneToRankerInfo(rankInSameGrade.getContent());
    }

    // 날짜 별 랭킹 전체 조회
    public List<RankerInformationDto> getRankerInformationByDateInAllGrade(String date, int page){
        Timestamp getDate = getTimeStampFromString(date);
        Slice<UsePhone> rankInAllGrade = useRepository.findRankInAllGrade(getDate, PageRequest.of(page * 25, 25));
        return moveUsePhoneToRankerInfo(rankInAllGrade.getContent());
    }

    // String -> Date(여기서 날짜 1일 더하기) -> timeStamp 로 변환
    public Timestamp getTimeStampFromString(String setDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(setDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            String new_date = sdf.format(cal.getTime()) + " 00:00:00";
            return Timestamp.valueOf(new_date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // String to int(목표시간, 이용시간)
    public TimeInfo stringToIntForTime(String time){
        String[] split = time.split(":");
        return TimeInfo.builder()
                .minutes(Integer.parseInt(split[0]) * 60 + Integer.parseInt(split[1])).build();
    }

    // String to int(날짜)
    public TimeInfo stringToIntForDate(String date){
        String[] split = date.split("-");
        return TimeInfo.builder()
                .year(Integer.parseInt(split[0]))
                .month(Integer.parseInt(split[1]))
                .day(Integer.parseInt(split[2]))
                .build();
    }

    // NullPointerException 해결
    public String preventNullError(Time time){
        if(ObjectUtils.isEmpty(time)){
            return "00:00:00";
        }
        else{
            return time.toString();
        }
    }

    public List<RankerInformationDto> moveUsePhoneToRankerInfo(List<UsePhone> list){
        AtomicInteger num = new AtomicInteger(1);
        return list.stream().map(r -> RankerInformationDto.builder()
                        .rankNum(num.getAndIncrement())
                        .nickname(r.getUser().getNickname())
                        .image(r.getUser().getImage())
                        .useTime(stringToIntForTime(preventNullError(r.getUseTime())))
                        .purposeTime(stringToIntForTime(preventNullError(r.getPurposeTime()))).build())
                .collect(Collectors.toList());
    }

}
