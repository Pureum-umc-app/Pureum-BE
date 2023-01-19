package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.GetGoalResultsRes;
import com.umc.pureum.domain.use.entity.Use;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
}
