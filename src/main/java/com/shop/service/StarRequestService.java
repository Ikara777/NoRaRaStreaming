package com.shop.service;

import com.shop.dto.StarRequestDto;
import com.shop.entity.Member;
import com.shop.entity.StarRequest;
import com.shop.repository.StarRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StarRequestService {


    private final StarRequestRepository starRequestRepository;
    private final RentService rentService;

    public void saveStarRequest(StarRequestDto starRequestDto) {
        StarRequest starRequest = new StarRequest();
        starRequest.setMember(starRequestDto.getMember());
        starRequest.setBankCode(starRequestDto.getBankCode());
        starRequest.setAccountNumber(starRequestDto.getAccountNumber());
        starRequest.setAccountHolder(starRequestDto.getAccountHolder());
        starRequest.setAmount(starRequestDto.getAmount());

        starRequestRepository.save(starRequest);
    }




}
