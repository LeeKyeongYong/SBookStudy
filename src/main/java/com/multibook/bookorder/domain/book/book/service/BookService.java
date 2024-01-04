package com.multibook.bookorder.domain.book.book.service;

import com.multibook.bookorder.domain.book.book.entity.Book;
import com.multibook.bookorder.domain.book.book.repository.BookRepository;
import com.multibook.bookorder.domain.member.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;

    @Transactional
    public Book createBook(Member author, String title, String body, int price, boolean published) {
        Book book = Book.builder()
                .author(author)
                .title(title)
                .body(body)
                .price(price)
                .published(published)
                .build();

        bookRepository.save(book);

        return book;
    }

    public Page<Book> search(Member author, Boolean published, List<String> kwTypes, String kw, Pageable pageable) {
        return bookRepository.search(author, published, kwTypes, kw, pageable);
    }
}