package com.multibook.bookorder.domain.member.myBook.entity;

import com.multibook.bookorder.domain.book.book.entity.Book;
import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.global.jpa.BaseTime;
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
@ToString(callSuper = true, exclude = "owner")
public class MyBook extends BaseTime {
    @ManyToOne
    private Member owner;
    @ManyToOne
    private Book book;
}