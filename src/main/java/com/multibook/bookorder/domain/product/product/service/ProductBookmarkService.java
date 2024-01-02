package com.multibook.bookorder.domain.product.product.service;

import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.product.product.entity.Product;
import com.multibook.bookorder.domain.product.product.entity.ProductBookmark;
import com.multibook.bookorder.domain.product.product.repository.ProductBookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductBookmarkService {
    private final ProductBookmarkRepository productBookmarkRepository;

    @Transactional
    public void bookmark(Member member, Product product) {
        ProductBookmark productBookmark = ProductBookmark.builder()
                .member(member)
                .product(product)
                .build();

        productBookmarkRepository.save(productBookmark);
    }

    @Transactional
    public void cancelBookmark(Member member, Product product) {
        productBookmarkRepository.deleteByMemberAndProduct(member, product);
    }

    public boolean canBookmark(Member actor, Product product) {
        if (actor == null) return false;

        return !productBookmarkRepository.existsByMemberAndProduct(actor, product);
    }

    public boolean canCancelBookmark(Member actor, Product product) {
        if (actor == null) return false;

        return productBookmarkRepository.existsByMemberAndProduct(actor, product);
    }

    public List<ProductBookmark> findByMember(Member member) {
        return productBookmarkRepository.findByMemberOrderByIdDesc(member);
    }
}
