package com.multibook.bookorder.domain.product.product.service;
import com.multibook.bookorder.domain.book.book.entity.Book;
import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.product.product.entity.Product;
import com.multibook.bookorder.domain.product.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductBookmarkService productBookmarkService;

    @Transactional
    public Product createProduct(Book book, boolean published) {
        if (book.getProduct() != null) return book.getProduct();

        Product product = Product.builder()
                .maker(book.getAuthor())
                .relTypeCode(book.getModelName())
                .relId(book.getId())
                .name(book.getTitle())
                .price(book.getPrice())
                .published(published)
                .build();

        productRepository.save(product);

        book.setProduct(product);

        return product;
    }

    public Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    public Page<Product> search(Member maker, Boolean published, List<String> kwTypes, String kw, Pageable pageable) {
        return productRepository.search(maker, published, kwTypes, kw, pageable);
    }

    public boolean canBookmark(Member actor, Product product) {
        if (actor == null) return false;

        return productBookmarkService.canBookmark(actor, product);
    }

    public boolean canCancelBookmark(Member actor, Product product) {
        if (actor == null) return false;

        return productBookmarkService.canCancelBookmark(actor, product);
    }

    @Transactional
    public void bookmark(Member member, Product product) {
        productBookmarkService.bookmark(member, product);
    }

    @Transactional
    public void cancelBookmark(Member member, Product product) {
        productBookmarkService.cancelBookmark(member, product);
    }
}