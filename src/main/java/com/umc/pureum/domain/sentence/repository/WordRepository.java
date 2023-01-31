package com.umc.pureum.domain.sentence.repository;

import com.umc.pureum.domain.sentence.entity.Word;
import com.umc.pureum.global.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface WordRepository extends JpaRepository<Word, Long> {
    /* 단어 반환 */
    List<Word> findAllByIdAfter(Long id);
}
