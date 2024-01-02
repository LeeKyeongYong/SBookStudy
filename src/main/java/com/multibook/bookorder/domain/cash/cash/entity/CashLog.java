package com.multibook.bookorder.domain.cash.cash.entity;

import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.global.jpa.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class CashLog extends BaseTime {
    @Enumerated(EnumType.STRING)
    private EvenType evenType;
    private String relTypeCode;
    private Long relId;
    @ManyToOne
    private Member member;
    private long price;

    public enum EvenType{
        충전__무통장입금,
        충전__토스페이먼츠,
        출금__통장입금,
        사용__토스페이먼츠_주문결제,
        사용__예치금_주문결제,
        환불__예치금_주문결제,
        작가정산__예치금;
    }
}
