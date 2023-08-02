package com.umc.pureum.domain.inquiry;

import com.umc.pureum.domain.inquiry.dto.PostInquiryReq;
import com.umc.pureum.domain.inquiry.entity.Inquiry;
import com.umc.pureum.domain.inquiry.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryService {
    private final InquiryRepository inquiryRepository;

    @Transactional
    public void saveInquiry(PostInquiryReq postInquiryReq){
        Inquiry inquiry = new Inquiry(postInquiryReq.getEmail(), postInquiryReq.getContent());
        inquiryRepository.save(inquiry);
    }

}
