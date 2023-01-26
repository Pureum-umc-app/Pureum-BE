package com.umc.pureum.domain.mypage;

import com.umc.pureum.domain.mypage.dto.PostUpdateSentenceReq;
import com.umc.pureum.domain.sentence.entity.Sentence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MyPageService {
    private final MyPageDao myPageDao;

    // 문장 수정
    @Transactional
    public void updateSentence(Long sentenceId,PostUpdateSentenceReq postUpdateSentenceReq){
        Sentence sentence = myPageDao.find(sentenceId);
        sentence.setSentence(postUpdateSentenceReq.getSentence());
    }

    // 문장 삭제(테이블에서 없애는 것이 아니라 상태만 바꿈)
    @Transactional
    public void deleteSentence(Long sentenceId){
        Sentence sentence = myPageDao.find(sentenceId);
        sentence.setStatus("D");
    }
}
