package com.multibook.bookorder.domain.product.cart.controller;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final Rq rq;
    private final CartService cartService;
    private final ProductService productServie;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showList() {
        List<CartItem> cartItems = cartService.findByBuyerOrderByIdDesc(rq.getMember());

        long totalPrice = cartItems
                .stream()
                .map(CartItem::getProduct)
                .mapToLong(Product::getPrice)
                .sum();

        rq.attr("totalPrice", totalPrice);
        rq.attr("cartItems", cartItems);

        return "domain/product/cart/list";
    }

    @PostMapping("/add/{id}")
    @PreAuthorize("isAuthenticated()")
    public String add(
            @PathVariable long id,
            @RequestParam(defaultValue = "/") String redirectUrl
    ) {
        Product product = productServie.findById(id).orElseThrow(() -> new GlobalException("400", "존재하지 않는 상품입니다."));
        cartService.addItem(rq.getMember(), product);

        return rq.redirect(redirectUrl, null);
    }

    @DeleteMapping("/remove/{id}")
    @PreAuthorize("isAuthenticated()")
    public String remove(
            @PathVariable long id,
            @RequestParam(defaultValue = "/") String redirectUrl
    ) {
        Product product = productServie.findById(id).orElseThrow(() -> new GlobalException("400", "존재하지 않는 상품입니다."));
        cartService.removeItem(rq.getMember(), product);

        return rq.redirect(redirectUrl, null);
    }
}