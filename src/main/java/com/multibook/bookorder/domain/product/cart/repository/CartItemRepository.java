package com.multibook.bookorder.domain.product.cart.repository;

import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.product.cart.entity.CartItem;
import com.multibook.bookorder.domain.product.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findBuyear(Member buyer);
    boolean existsBuyearAndProduct(Member buyer, Product product);
    void deleteAllByBuyerAndProduct(Member buyer,Product product);
}
