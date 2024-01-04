package com.multibook.bookorder.domain.book.book.repository;

import com.multibook.bookorder.domain.book.book.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long>, BookRepositoryCustom {

}