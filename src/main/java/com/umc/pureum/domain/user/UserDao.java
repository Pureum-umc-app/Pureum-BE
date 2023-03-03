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

    // 사용자 테이블 단건 조회
    public UserAccount find(Long id){
        return em.find(UserAccount.class,id);
    }

    // userId 로 UserAccount 찾기
    public UserAccount findByUserId(Long userId){
        return em.find(UserAccount.class,userId);
    }

    public List<UserAccount> findAllExcludeMe(Long id) {
        return em.createQuery("select u from UserAccount u where u.id != :id and u.status = 'A' order by RAND()", UserAccount.class)
                .setParameter("id", id)
                .setMaxResults(20)
                .getResultList();
    }
}
