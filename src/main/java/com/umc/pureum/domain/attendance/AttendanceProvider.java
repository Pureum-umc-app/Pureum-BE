package com.umc.pureum.domain.attendance;

import com.umc.pureum.domain.attendance.dto.GetStampRes;
import com.umc.pureum.domain.attendance.entity.AttendanceCheck;
import com.umc.pureum.domain.attendance.entity.AttendanceStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceProvider {
    private final AttendanceDao attendanceDao;
    private final AttendanceRepository attendanceRepository;

    /* 도장 개수 반환 API */
    public GetStampRes getStamps(Long userIdx) {
        List<AttendanceCheck> attendanceChecks = attendanceRepository.findALLByUserIdAndStatus(userIdx, AttendanceStatus.A);

        return new GetStampRes(attendanceChecks.size(), attendanceChecks.size() % 30);
    }
}
