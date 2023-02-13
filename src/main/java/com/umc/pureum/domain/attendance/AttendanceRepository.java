package com.umc.pureum.domain.attendance;

import com.umc.pureum.domain.attendance.dto.response.GetStampInterface;
import com.umc.pureum.domain.attendance.entity.AttendanceCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface AttendanceRepository extends JpaRepository<AttendanceCheck, Long> {
    /* 도장 개수 반환 */
    @Query("select count(a.id) as accumulatedCnt, mod(count(a.id), 30) as currentCnt \n" +
            "from AttendanceCheck as a \n" +
            "where a.user.id = :id and a.user.status = 'A' \n" +
            "   and a.status = 'A'")
    GetStampInterface findByUserIdAndStatus(@Param("id") Long id);

    List<AttendanceCheck> findByUserId(long userId);
}
