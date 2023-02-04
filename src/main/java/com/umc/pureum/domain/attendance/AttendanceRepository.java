package com.umc.pureum.domain.attendance;

import com.umc.pureum.domain.attendance.dto.GetStampRes;
import com.umc.pureum.domain.attendance.entity.AttendanceCheck;
import com.umc.pureum.domain.attendance.entity.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface AttendanceRepository extends JpaRepository<AttendanceCheck, Long> {
    List<AttendanceCheck> findALLByUserIdAndStatus(Long id, AttendanceStatus status);

    List<AttendanceCheck> findByUserId(long userId);
}
