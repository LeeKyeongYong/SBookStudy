package com.multibook.bookorder.global.initData;


import com.multibook.bookorder.domain.book.book.entity.Book;
import com.multibook.bookorder.domain.book.book.service.BookService;
import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.member.member.service.MemberService;
import com.multibook.bookorder.domain.product.cart.service.CartService;
import com.multibook.bookorder.domain.product.order.service.OrderService;
import com.multibook.bookorder.domain.product.product.entity.Product;
import com.multibook.bookorder.domain.product.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
@RequiredArgsConstructor
public class NotProd {//실행하면 데이터 insert 시키는 클래스

    @Autowired
    @Lazy
    private NotProd self;
    private final MemberService memberService;
    private final BookService bookService;
    private final ProductService productService;
    private final CartService cartService;
    private final OrderService orderService;

    @Bean
    @org.springframework.core.annotation.Order(3)
    ApplicationRunner initNotProd(){
        return args -> {
          self.work1();
          self.work2();
        };
    }

    @Transactional
    public void work1(){
        Member memberAdmin = memberService.join("admin","1234","관리자").getData();
        Member memberUser1 = memberService.join("user1","1234","유저1").getData();
        Member memberUser2 = memberService.join("user2","1234","유저2").getData();
        Member memberUser3 = memberService.join("user3","1234","유저3").getData();
        Member memberUser4 = memberService.join("user4","1234","유저4").getData();
        Member memberUser5 = memberService.join("user5","1234","유저5").getData();

        Book book1 = bookService.createBook(memberUser1,"책 제목 1","책 내용 1","10_000");
        Book book2 = bookService.createBook(memberUser2,"책 제목 2","책 내용 2","20_000");
        Book book3 = bookService.createBook(memberUser3,"책 제목 3","책 내용 3","30_000");
        Book book4 = bookService.createBook(memberUser3,"책 제목 4","책 내용 4","40_000");
        Book book5 = bookService.createBook(memberUser3,"책 제목 5","책 내용 5","50_000");
        Book book6 = bookService.createBook(memberUser2,"책 제목 6","책 내용 6","60_000");

        
    }
    @Transactional
    public void work2(){
        //Member memberUser1 = memberService.findByUsername("user1").get();
        //Product product1 = productService.findById(1L).get();
        //cartService.addItem(memberUser1,product1);
    }
}
