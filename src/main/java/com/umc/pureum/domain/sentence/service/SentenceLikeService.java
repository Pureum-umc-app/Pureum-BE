package com.umc.pureum.domain.sentence.service;

import com.umc.pureum.domain.sentence.entity.mapping.SentenceLikeMapping;
import com.umc.pureum.domain.sentence.repository.SentenceLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * packageName    : com.umc.pureum.domain.sentence.service
 * fileName       : SentenceLikeService
 * author         : peter
 * date           : 2023-01-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-01-30        peter       최초 생성
 */
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SentenceLikeService {
    private final SentenceLikeRepository sentenceLikeRepository;
    public List<SentenceLikeMapping> getSentenceLikeOrderByDate(long word_id, int page, int limit, String sort){
        List<SentenceLikeMapping> sentenceListRes = new ArrayList<>();
        if(sort.equals("date")) {
            sentenceListRes = sentenceLikeRepository.findSentenceLikePageOrderByDate(word_id, PageRequest.of(page, limit, Sort.Direction.DESC, "sentence_id"));
        }
        else if(sort.equals("like")){
            sentenceListRes = sentenceLikeRepository.findSentenceLikePageOrderByDate(word_id, PageRequest.of(page, limit, Sort.Direction.DESC, "likeNum"));
        }
        return sentenceListRes;
    }

    public boolean getSentenceSelfLike(long userId, long sentenceId){
        byte level = sentenceLikeRepository.ExistsByUserIdAndSentenceId(userId, sentenceId);
        if( level==1){
            return true;
        }
        else  return false;
    }
}
