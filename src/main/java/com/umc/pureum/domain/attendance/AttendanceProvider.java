package com.umc.pureum.domain.attendance;

import com.umc.pureum.domain.attendance.dto.response.GetStampInterface;
import com.umc.pureum.domain.attendance.dto.response.GetStampRes;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.config.Response.BaseException;
import com.umc.pureum.global.config.Response.BaseResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceProvider {
    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;

    /* 도장 개수 반환 API */
    public GetStampRes getStamps(Long userId) throws BaseException {
        // 존재하는 회원인지 검사
        Optional<UserAccount> user = userRepository.findByIdAndStatus(userId, "A");
        if(user.isEmpty()) throw new BaseException(BaseResponseStatus.INVALID_USER);

        // 출석 개수를 받아옴
        GetStampInterface stamp = attendanceRepository.findByUserIdAndStatus(userId);

        return new GetStampRes(userId, stamp.getAccumulatedCnt(), stamp.getCurrentCnt());
    }
}
