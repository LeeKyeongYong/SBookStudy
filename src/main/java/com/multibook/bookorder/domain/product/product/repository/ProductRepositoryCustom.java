package com.multibook.bookorder.domain.product.product.repository;

import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.product.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface ProductRepositoryCustom {
    Page<Product> search(Member maker, Boolean published, List<String> kwTypes, String kw, Pageable pageable);
}
