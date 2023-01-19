package com.umc.pureum.domain.use;


import com.umc.pureum.domain.use.dto.GetUseTimeAndCountRes;
import com.umc.pureum.domain.use.entity.Use;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.transform.Result;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UseProvider {
    private final UseDao useDao;

    // 일일 사용시간 & 화면 킨 횟수 조회(회원가입부터 지금까지)
//    public  getTimeAndCount(Long user_id){
//        List<Use> useAll = useDao.findAll(user_id);
//    }
}
