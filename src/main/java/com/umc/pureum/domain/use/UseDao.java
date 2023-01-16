package com.umc.pureum.domain.use;

import com.umc.pureum.domain.use.dto.PostUseTimeAndCountReq;
import com.umc.pureum.domain.use.entity.Use;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class UseDao {
    private final EntityManager em;

    // 유저 사용 테이블 단건 조회
    public Use findOne(Long user_id){
        return em.find(Use.class,user_id);
    }


}
