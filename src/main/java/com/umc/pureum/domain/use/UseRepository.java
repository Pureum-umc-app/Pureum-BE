package com.umc.pureum.domain.use;

import com.umc.pureum.domain.use.entity.UsePhone;
import com.umc.pureum.domain.use.entity.UseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@EnableJpaRepositories
public interface UseRepository extends JpaRepository<UsePhone, Long> {
    /**
     * 목표 달성 여부 반환
     * updated_at이 현재 날짜랑 같은 데이터까지 반환
     **/

    @Query("select u from UsePhone u where u.user.id = :id " +
            "and u.updatedAt <= :today " +
            "and u.status = 'A'")
    List<UsePhone> findAllByConditions(@Param("id") Long id, @Param("today") Timestamp today);

}
