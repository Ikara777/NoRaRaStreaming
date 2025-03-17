package com.shop.service;


import com.shop.constant.PaymentStatus;
import com.shop.dto.RentDto;
import com.shop.entity.Member;
import com.shop.entity.Payment;
import com.shop.entity.Rent;
import com.shop.repository.RentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RentService {

    @Autowired
    private final PaymentService paymentService;
    private final CashSummaryService cashSummaryService;
    private final RentRepository rentRepository;


    @Transactional
    public Rent submitRent(RentDto rentDto, Member member){

        Rent rent = new Rent();
        rent.setRentAmount(rentDto.getRentAmount());
        rent.setMember(member);
        rent.setRentTime(LocalDateTime.now());
        rent.setStatus(PaymentStatus.PAID);
        rent.setNickname(rentDto.getNickName());
        rent.setMessage(rentDto.getMassage());

        cashSummaryService.UpdateCashSummary(member.getEmail(), member.getName());

        return rentRepository.save(rent);
    }


    public Integer getTotalAmountByMemberId(Long memberId) {
        // Repository에서 합산 쿼리 호출
        return rentRepository.sumRentAmountByMemberId( memberId ).orElse(0);
    }


    @Transactional
    public Long processRefund(Long rentId, String email, String name) {

        Rent rent = rentRepository.findById(rentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 결제 내역이 없습니다. id=" + rentId));

        // 이미 취소된 경우 체크
        if (rent.getStatus() == PaymentStatus.CANCEL) {
            throw new IllegalStateException("이미 환불된 결제입니다.");
        }

        // 상태를 CANCEL로 변경
        rent.setStatus(PaymentStatus.CANCEL);

        //캐시 합산 다시 연산
        cashSummaryService.UpdateCashSummary(email, name);

        return rentId;
    }


    @Transactional
    public void updateToREFUND(List<Rent> rentList) {
        rentRepository.updateStatusForRentList(rentList, PaymentStatus.REFUND);
    }

    public Page<Rent> getRentList(Long member, Pageable pageable) {
        return rentRepository.findByMemberIdOrderByIdDesc(member, pageable);
    }



}
