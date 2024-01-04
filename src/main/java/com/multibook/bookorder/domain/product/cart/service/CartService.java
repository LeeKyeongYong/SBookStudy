package com.multibook.bookorder.domain.product.cart.service;

import com.multibook.bookorder.domain.global.exceptions.GlobalException;
import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.product.cart.entity.CartItem;
import com.multibook.bookorder.domain.product.cart.repository.CartItemRepository;
import com.multibook.bookorder.domain.product.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CartService {
    private final CartItemRepository cartItemRepository;

    @Transactional
    public CartItem addItem(Member buyer, Product product) {
        if (buyer.has(product))
            throw new GlobalException("400-1", "이미 구매한 상품입니다.");

        CartItem cartItem = CartItem.builder()
                .buyer(buyer)
                .product(product)
                .build();

        cartItemRepository.save(cartItem);

        return cartItem;
    }

    @Transactional
    public void removeItem(Member buyer, Product product) {
        cartItemRepository.deleteByBuyerAndProduct(buyer, product);
    }

    public List<CartItem> findByBuyerOrderByIdDesc(Member buyer) {
        return cartItemRepository.findByBuyer(buyer);
    }

    public void delete(CartItem cartItem) {
        cartItemRepository.delete(cartItem);
    }

    public boolean canAdd(Member buyer, Product product) {
        if (buyer == null) return false;

        return !cartItemRepository.existsByBuyerAndProduct(buyer, product);
    }

    public boolean canRemove(Member buyer, Product product) {
        if (buyer == null) return false;

        return cartItemRepository.existsByBuyerAndProduct(buyer, product);
    }

    public boolean canDirectMakeOrder(Member buyer, Product product) {
        return canAdd(buyer, product);
    }
}