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

    // 사용자 테이블 중 유저 아이디 조회
    public List<UserAccount> findAll(){
        return em.createQuery("select u from UserAccount u", UserAccount.class)
                .getResultList();
    }

}
