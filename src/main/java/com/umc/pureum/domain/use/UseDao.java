package com.umc.pureum.domain.use;

import com.umc.pureum.domain.use.entity.UsePhone;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UseDao {
    private final EntityManager em;

    // 사용 테이블 등록
    public void save(UsePhone use){
        em.persist(use);
    }

    // 사용 테이블의 외래키를 통한 최근에 생성된 사용 테이블 단건 조회
    public UsePhone findOneByFk(Long userId){
        return em.createQuery("select u from UsePhone u " +
                        "join fetch u.user m " +
                        "where m.id = :userId order by u.createdAt desc", UsePhone.class)
                .setParameter("userId",userId)
                .setMaxResults(1)
                .getSingleResult();
    }

    // 사용 테이블의 외래키를 통한 사용 테이블 모두 조회
    public List<UsePhone> findAll(Long user_id){
        return em.createQuery("select u from UsePhone u " +
                        "join fetch u.user m " +
                        "where m.id = :user_id", UsePhone.class)
                .setParameter("user_id",user_id)
                .getResultList();
    }

    // 날짜별 사용시간 적은 순 사용 top 10(같은 카테고리 내) 가져오기
    public List<UsePhone> findRankTopTen(Timestamp updateAt, int grade){
        return em.createQuery("select u from UsePhone u " +
                        "join fetch u.user m " +
                        "where u.updatedAt = :updateAt " +
                        "and m.grade = :grade order by u.useTime", UsePhone.class)
                .setParameter("updateAt", updateAt)
                .setParameter("grade", grade)
                .setMaxResults(10)
                .getResultList();
    }

    // 날짜별 사용시간 적은 순 같은 학년 내 사용 랭킹(페이지 0 일 경우) 가져오기
    public List<UsePhone> findRankZeroInSameGrade(Timestamp updateAt, int grade){
        return em.createQuery("select u from UsePhone u " +
                        "join fetch u.user m " +
                        "where u.updatedAt = :updateAt " +
                        "and m.grade = :grade order by u.useTime", UsePhone.class)
                .setParameter("updateAt", updateAt)
                .setParameter("grade", grade)
                .setFirstResult(0)
                .setMaxResults(25)
                .getResultList();
    }

    // 날짜별 사용시간 적은 순 같은 학년 내 사용 랭킹(페이지 0 초과) 가져오기
    public List<UsePhone> findRankOverZeroInSameGrade(Timestamp updateAt, int grade, int page){
        return em.createQuery("select u from UsePhone u " +
                        "join fetch u.user m " +
                        "where u.updatedAt = :updateAt " +
                        "and m.grade = :grade order by u.useTime", UsePhone.class)
                .setParameter("updateAt", updateAt)
                .setParameter("grade", grade)
                .setFirstResult(page*25)
                .setMaxResults(25)
                .getResultList();
    }

    // 날짜별 사용시간 적은 순 사용 랭킹(페이지 0 일 경우) 가져오기
    public List<UsePhone> findRankZeroInAllGrade(Timestamp updateAt){
        return em.createQuery("select u from UsePhone u " +
                        "join fetch u.user m " +
                        "where u.updatedAt = :updateAt order by u.useTime", UsePhone.class)
                .setParameter("updateAt", updateAt)
                .setFirstResult(0)
                .setMaxResults(25)
                .getResultList();
    }

    // 날짜별 사용시간 적은 순 사용 랭킹(페이지 0 초과) 가져오기
    public List<UsePhone> findRankOverZeroInAllGrade(Timestamp updateAt, int page){
        return em.createQuery("select u from UsePhone u " +
                        "join fetch u.user m " +
                        "where u.updatedAt = :updateAt order by u.useTime", UsePhone.class)
                .setParameter("updateAt", updateAt)
                .setFirstResult(page*25)
                .setMaxResults(25)
                .getResultList();
    }



 }
