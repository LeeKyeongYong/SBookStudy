package com.multibook.bookorder.domain.product.product.repository;

import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.product.product.entity.Product;
import com.multibook.bookorder.domain.product.product.entity.ProductBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductBookmarkRepository extends JpaRepository<ProductBookmark, Long> {
    boolean existsByMemberAndProduct(Member actor, Product product);

    void deleteByMemberAndProduct(Member member, Product product);

    List<ProductBookmark> findByMemberOrderByIdDesc(Member member);
}