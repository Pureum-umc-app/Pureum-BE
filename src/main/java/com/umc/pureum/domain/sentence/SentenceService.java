package com.umc.pureum.domain.sentence;

import com.umc.pureum.domain.sentence.entity.Keyword;
import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.domain.sentence.repository.KeywordRepository;
import com.umc.pureum.domain.sentence.repository.WordRepository;
import com.umc.pureum.global.entity.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SentenceService {
    private final SentenceDao sentenceDao;
    private final WordRepository wordRepository;
    private final KeywordRepository keywordRepository;

    /**
     * 매일 0시에 오늘의 단어 3개를 불러옴
     * word에서 받아와서 keyword에 저장
     */
    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void getKeyword() {
        System.out.println("start");
        List<Word> words = new ArrayList<>();

        while(words.size() < 3) {  // 하루에 최대 3개까지 리턴
            // 랜덤 값을 생성
            Random random = new Random();
            Long id = (long) (random.nextInt(4300) + 1);

            // 단어를 받아옴
            Optional<Word> word = wordRepository.findById(id);

            if(word.isPresent()) {
                // 단어가 존재하면 keyword에 존재하는지 검사
                Optional<Keyword> keyword = keywordRepository.findByWordId(id);

                if(keyword.isEmpty()) {
                    // Keyword 테이블에 존재하는지 검사 후 없으면 넣기
                    keywordRepository.save(new Keyword(word.get(), "A"));
                    words.add(word.get());
                }
            }
        }
    }
}
