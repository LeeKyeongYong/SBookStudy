package com.multibook.bookorder.domain.book.book.controller;

import com.multibook.bookorder.domain.book.book.entity.Book;
import com.multibook.bookorder.domain.book.book.service.BookService;
import com.multibook.bookorder.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;
    private final Rq rq;

    @GetMapping("/list")
    public String showList(
            @RequestParam(value="kwType",defaultValue = "title")List<String> kwTypes,
            @RequestParam(value="") String kw,@RequestParam(defaultValue = "1") int page,Model model){

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page-1,10,Sort.by(sorts));

        Map<String,Boolean> kwTypesMap = kwTypes
                .stream().collect(Collectors.toMap(kwType -> kwType,kwType -> true));

        Page<Book> itemsPage = bookService.search(null,true,kwTypes,kw,pageable);
        model.addAttribute("itemPage",itemsPage);
        model.addAttribute("kwTypesMap",kwTypesMap);
        model.addAttribute("page",page);

        return "domain/book/book/list";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/myList")
    public String showMyList(
            @RequestParam(value="kwType",defaultValue = "title") List<String> kwTypes,
            @RequestParam(defaultValue = "")String kw,@RequestParam(defaultValue = "1") int page,Model model){

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page-1,10,Sort.by(sorts));

        Map<String,Boolean> kwTypesMap = kwTypes
                .stream().collect(Collectors.toMap(kwType -> kwType,kwType -> true));

        Page<Book> itemsPage = bookService.search(rq.getMember(), null, kwTypes, kw, pageable);
        model.addAttribute("itemPage",itemsPage);
        model.addAttribute("kwTypesMap",kwTypesMap);
        model.addAttribute("page",page);

        return "domain/book/book/list";
    }

}
