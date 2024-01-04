package com.multibook.bookorder.domain.product.cart.repository;

import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.product.cart.entity.CartItem;
import com.multibook.bookorder.domain.product.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByBuyer(Member buyer);

    boolean existsByBuyerAndProduct(Member buyer, Product product);

    void deleteByBuyerAndProduct(Member buyer, Product product);
}
