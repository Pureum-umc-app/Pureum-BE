package com.umc.pureum.domain.use;


<<<<<<< HEAD
import com.umc.pureum.domain.use.dto.GetUseTimeAndCountRes;
=======
import com.umc.pureum.domain.use.dto.GetGoalResultsRes;
>>>>>>> caf7cab2eca6b311f9881f08bff27d544c05613f
import com.umc.pureum.domain.use.entity.Use;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

<<<<<<< HEAD
import javax.xml.transform.Result;
import java.util.List;
=======
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
>>>>>>> caf7cab2eca6b311f9881f08bff27d544c05613f

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UseProvider {
    private final UseDao useDao;
<<<<<<< HEAD

    // 일일 사용시간 & 화면 킨 횟수 조회(회원가입부터 지금까지)
//    public  getTimeAndCount(Long user_id){
//        List<Use> useAll = useDao.findAll(user_id);
//    }
=======
    private final UseRepository useRepository;

    /** API **/

    /* 목표 달성 여부 반환 API */
//    public List<GetGoalResultsRes> getGoalResults(int userIdx) {
//        Date now = new Date();
//        Timestamp timestamp = new Timestamp(now.getTime());
//
////        List<Use> uses = useRepository.findAllByUser(userIdx);
//
//
//        return uses.stream()
//                .map(d -> GetGoalResultsRes.builder()
//                        .date(d.getUpdated_at().format(DateTimeFormatter.ofPattern("YYYY-MM-DD")))
//                        .isSuccess(getSuccess(d.getUse_time(), d.getPurpose_time())).build())
//                .collect(Collectors.toList());
//    }

    public int getSuccess(Time use_time, Time purpose_time) {
        if(use_time.after(purpose_time)) {
            return 0;  // 실패
        } else {
            return 1;  // 성공
        }
    }


    /** 유효성 검사 **/
>>>>>>> caf7cab2eca6b311f9881f08bff27d544c05613f
}
