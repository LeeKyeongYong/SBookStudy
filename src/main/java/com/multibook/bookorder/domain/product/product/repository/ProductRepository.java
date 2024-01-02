package com.multibook.bookorder.domain.product.product.repository;

import com.multibook.bookorder.domain.product.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long>,ProductRepositoryCustom  {
}
