package com.umc.pureum.domain.attendance;

import com.umc.pureum.domain.attendance.dto.request.AttendanceCheckReq;
import com.umc.pureum.domain.attendance.dto.response.AttendanceCheckRes;
import com.umc.pureum.domain.attendance.entity.AttendanceCheck;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.global.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttendanceService {
    private final UserRepository userRepository;
    private final AttendanceDao attendanceDao;

    // check : 문장 좋아요 DB 에 저장
    @Transactional
    public AttendanceCheckRes checkAttendance(AttendanceCheckReq request) {

        // request 로 받은 userId 로 userAccount 찾기
        UserAccount userAccount = userRepository.findById(request.getUserId()).get();

        // attendanceCheck 생성
        AttendanceCheck attendanceCheck = new AttendanceCheck(userAccount, Status.A);

        // attendanceCheck DB 에 저장
        attendanceDao.save(attendanceCheck);

        return new AttendanceCheckRes(attendanceCheck.getId());

    }

}
