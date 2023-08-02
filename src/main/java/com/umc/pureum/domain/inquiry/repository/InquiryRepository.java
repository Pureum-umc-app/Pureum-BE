package com.umc.pureum.domain.inquiry.repository;

import com.umc.pureum.domain.inquiry.entity.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableJpaRepositories
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {



}
