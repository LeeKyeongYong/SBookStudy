package com.multibook.bookorder.domain.product.product.controller;

import com.multibook.bookorder.domain.product.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final Rq rq;
    private final ProductService productService;
    private final ProductBookmarkService productBookmarkService;

    @GetMapping("/bookmarkList")
    public String showBookmarkList() {
        List<ProductBookmark> productBookmarks = productBookmarkService.findByMember(rq.getMember());

        rq.attr("productBookmarks", productBookmarks);

        return "domain/product/product/bookmarkList";
    }

    @GetMapping("/list")
    public String showList(
            @RequestParam(value = "kwType", defaultValue = "name") List<String> kwTypes,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "1") int page,
            Model model
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));

        Map<String, Boolean> kwTypesMap = kwTypes
                .stream()
                .collect(Collectors.toMap(
                        kwType -> kwType,
                        kwType -> true
                ));

        Page<Product> itemsPage = productService.search(null, true, kwTypes, kw, pageable);
        model.addAttribute("itemPage", itemsPage);
        model.addAttribute("kwTypesMap", kwTypesMap);
        model.addAttribute("page", page);

        return "domain/product/product/list";
    }

    @GetMapping("/myList")
    public String showMyList(
            @RequestParam(value = "kwType", defaultValue = "name") List<String> kwTypes,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "1") int page,
            Model model
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));

        Map<String, Boolean> kwTypesMap = kwTypes
                .stream()
                .collect(Collectors.toMap(
                        kwType -> kwType,
                        kwType -> true
                ));

        Page<Product> itemsPage = productService.search(rq.getMember(), null, kwTypes, kw, pageable);
        model.addAttribute("itemPage", itemsPage);
        model.addAttribute("kwTypesMap", kwTypesMap);
        model.addAttribute("page", page);

        return "domain/product/product/myList";
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String showDetail(@PathVariable long id) {
        return null;
    }

    @PostMapping("/{id}/bookmark")
    @PreAuthorize("isAuthenticated()")
    public String bookmark(
            @PathVariable long id,
            @RequestParam(defaultValue = "/") String redirectUrl
    ) {
        Product product = productService.findById(id).orElseThrow(() -> new GlobalException("400", "존재하지 않는 상품입니다."));
        productService.bookmark(rq.getMember(), product);

        return rq.redirect(redirectUrl, null);
    }

    @DeleteMapping("/{id}/cancelBookmark")
    @PreAuthorize("isAuthenticated()")
    public String cancelBookmark(
            @PathVariable long id,
            @RequestParam(defaultValue = "/") String redirectUrl
    ) {
        Product product = productService.findById(id).orElseThrow(() -> new GlobalException("400", "존재하지 않는 상품입니다."));
        productService.cancelBookmark(rq.getMember(), product);

        return rq.redirect(redirectUrl, null);
    }
}