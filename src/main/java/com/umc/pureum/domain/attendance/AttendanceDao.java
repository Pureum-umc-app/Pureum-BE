package com.umc.pureum.domain.attendance;

import com.umc.pureum.domain.attendance.entity.AttendanceCheck;
import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Sentence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendanceDao {

    private final EntityManager em;

    // 출석 체크 저장
    public void save(AttendanceCheck attendanceCheck) {
        em.persist(attendanceCheck);
    }

    // 출석 체크 단건 조회
    public AttendanceCheck findOne(Long id){
        return em.find(AttendanceCheck.class, id);
    }

}
