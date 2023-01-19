package com.umc.pureum.domain.use;

import com.umc.pureum.domain.use.dto.GetGoalResultsRes;
import com.umc.pureum.domain.use.entity.Use;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface UseRepository extends JpaRepository<Use, Long> {
    /**
     * 목표 달성 여부 반환
     * updated_at이 현재 날짜랑 같은 데이터까지 반환
     **/
    //List<Use> findAllByUserAndUpdated_atBefore(int id);
}
