package com.multibook.bookorder.domain.product.cart.entity;

import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.product.product.entity.Product;
import com.multibook.bookorder.global.jpa.BaseTime;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;
com.multibook.bookorder.domain.member.member.entity.Member;

import static lombok.AccessLevel.PROTECTED;
public class CartItem extends BaseTime {
    @ManyToOne
    private Member buyer;
    @ManyToOne
    private Product product;

}
