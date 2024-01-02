package com.multibook.bookorder.domain.product.product.entity;

import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.global.jpa.BaseEntity;
import jakarta.persistence.Entity;
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
public class ProductBookmark extends BaseEntity {

    @ManyToOne
    private Member member;
    @ManyToOne
    private Product product;

}
