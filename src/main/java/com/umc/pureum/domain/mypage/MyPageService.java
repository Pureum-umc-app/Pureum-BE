package com.umc.pureum.domain.mypage;

import com.umc.pureum.domain.mypage.dto.request.PostUpdateSentenceReq;
import com.umc.pureum.domain.mypage.dto.request.PatchEditProfileReq;
import com.umc.pureum.domain.sentence.entity.Sentence;
import com.umc.pureum.domain.sentence.repository.SentenceRepository;
import com.umc.pureum.domain.user.UserRepository;
import com.umc.pureum.domain.user.entity.UserAccount;
import com.umc.pureum.domain.user.service.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MyPageService {
    private final MyPageDao myPageDao;
    private final UserRepository userRepository;
    private final S3Service s3Service;
    private final SentenceRepository sentenceRepository;

    // 문장 수정
    @Transactional
    public void updateSentence(Long sentenceId, PostUpdateSentenceReq postUpdateSentenceReq) {
        Optional<Sentence> sentence = sentenceRepository.findById(sentenceId);
        sentence.get().EditSentence(postUpdateSentenceReq.getSentence());
    }

    // 키워드 단어 가져오기
    public String getKeyword(Long sentenceId){
        Sentence sentence = myPageDao.find(sentenceId);
        String word = sentence.getKeyword().getWord().getWord();
        return word;
    }

    // 문장 삭제(테이블에서 없애는 것이 아니라 상태만 바꿈)
    @Transactional
    public void deleteSentence(Long sentenceId){
        Optional<Sentence> sentence = sentenceRepository.findById(sentenceId);
        sentence.get().DeleteSentence("D");
    }

    //프로필 수정
    @Transactional
    public void EditProfile(PatchEditProfileReq patchEditProfileReq, long id) throws IOException {
        UserAccount userAccount = userRepository.findByIdAndStatus(id,"A").orElseThrow(RuntimeException::new);
        userAccount.setImage(patchEditProfileReq.getImage()==null?null : s3Service.uploadFile(patchEditProfileReq.getImage()));
        userAccount.setNickname(patchEditProfileReq.getNickname());
    }
}
