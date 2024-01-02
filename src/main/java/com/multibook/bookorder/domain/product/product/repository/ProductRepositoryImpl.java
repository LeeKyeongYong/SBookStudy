package com.multibook.bookorder.domain.product.product.repository;

import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.product.product.entity.Product;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import java.util.ArrayList;
import java.util.List;

import static com.multibook.bookorder.domain.product.product.entity.Product;

@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Product> search(Member member, Boolean published, List<String> kwTypes, String kw, Pageable pageable) {
        return null;
    }
}
