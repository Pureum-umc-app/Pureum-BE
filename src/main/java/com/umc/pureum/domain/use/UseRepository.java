package com.umc.pureum.domain.use;

import com.umc.pureum.domain.use.entity.UsePhone;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
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
    @Query(value = "select u from UsePhone u " +
            "where date(u.createdAt) = current_date " +
            "and u.user.id = :id " +
            "and u.status = 'A'")
    List<UsePhone> existUsageTime(@Param("id")Long id, Pageable limitOne);

    List<UsePhone> findByUserId(Long id);

    @EntityGraph(attributePaths = {"user"})
    UsePhone findTop1ByUserIdOrderByCreatedAtDesc(Long id);

    @EntityGraph(attributePaths = {"user"})
    List<UsePhone> findTop10ByUpdatedAtAndUser_GradeOrderByUseTime(Timestamp updatedAt, int grade);

    @EntityGraph(attributePaths = {"user"})
    Slice<UsePhone> findByUpdatedAtAndUser_GradeOrderByUseTime(Timestamp updatedAt, int grade, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Slice<UsePhone> findByUpdatedAtOrderByUseTime(Timestamp updatedAt, Pageable pageable);

}
