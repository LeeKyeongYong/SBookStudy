package com.multibook.bookorder.domain.product.product.entity;

import com.multibook.bookorder.domain.book.book.entity.Book;
import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.global.app.AppConfig;
import com.multibook.bookorder.global.jpa.BaseTime;
import com.querydsl.core.annotations.QueryEntity;
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
@QueryEntity
@ToString(callSuper = true)
public class Product extends BaseTime{
    @ManyToOne
    private Member maker;
    private String relTypeCode;
    private long relId;
    private String name;
    private int price;
    private boolean published;

    public Book getBook(){
        return AppConfig.getEntityManager().getReference(Book.class,relId);
    }
}
