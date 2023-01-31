package com.umc.pureum.domain.user;


import com.umc.pureum.domain.user.entity.UserAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDao {

    private final EntityManager em;

    // 사용자 테이블 전체 조회(해당 유저 제외한) + 랜덤
    public List<UserAccount> findAllExcludeMe(Long id){
        return em.createQuery("select u from UserAccount u where u.id != :id and u.status = 'A' order by RAND()", UserAccount.class)
                .setParameter("id",id)
                .setMaxResults(20)
                .getResultList();
    }

    // userId 로 UserAccount 찾기
    public UserAccount findByUserId(Long userId){
        return em.find(UserAccount.class,userId);
    }
}
