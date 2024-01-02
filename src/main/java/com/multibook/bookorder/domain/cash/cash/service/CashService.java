package com.multibook.bookorder.domain.cash.cash.service;

import com.multibook.bookorder.domain.cash.cash.entity.CashLog;
import com.multibook.bookorder.domain.cash.cash.repository.CashLogRepository;
import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.global.jpa.BaseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CashService {
    private final CashLogRepository cashLogRepository;

    @Transactional
    public CashLog addCash(Member member, long price, CashLog.EvenType eventType, BaseEntity relEntity) {
        CashLog cashLog = CashLog.builder()
                .member(member)
                .price(price)
                .relTypeCode(relEntity.getModelName())
                .relId(relEntity.getId())
                .eventType(eventType)
                .build();

        cashLogRepository.save(cashLog);

        return cashLog;
    }
}
