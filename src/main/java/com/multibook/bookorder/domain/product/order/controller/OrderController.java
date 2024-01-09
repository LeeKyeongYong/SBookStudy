package com.multibook.bookorder.domain.product.order.controller;

@Controller
@PreAuthorize("isAuthenticated()")
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final Rq rq;
    private final OrderService orderService;
    private final ProductService productService;

    @PostMapping("/directMakeOrder/{productId}")
    public String directMakeOrder(
            @PathVariable long productId
    ) {
        Product product = productService.findById(productId)
                .orElseThrow(() -> new GlobalException("400", "존재하지 않는 상품입니다."));

        Order order = orderService.createFromProduct(rq.getMember(), product);

        return rq.redirect("/order/" + order.getId(), "주문이 완료되었습니다.");
    }

    @PostMapping("/createFromCart")
    public String createFromCart() {
        Order order = orderService.createFromCart(rq.getMember());

        return rq.redirect("/order/" + order.getId(), "주문이 완료되었습니다.");
    }

    @DeleteMapping("/{id}/cancel")
    public String cancel(
            @PathVariable long id,
            String redirectUrl
    ) {
        Order order = orderService.findById(id).orElse(null);

        if (order == null) {
            throw new GlobalException("404", "존재하지 않는 주문입니다.");
        }

        if (!orderService.canCancel(rq.getMember(), order)) {
            throw new GlobalException("403", "권한이 없습니다.");
        }

        orderService.cancel(order);

        if (Ut.str.isBlank(redirectUrl)) {
            redirectUrl = "/order/" + order.getId();
        }

        return rq.redirect(redirectUrl, "주문이 취소되었습니다.");
    }

    @GetMapping("/myList")
    public String showMyList(
            @RequestParam(defaultValue = "1") int page,
            Boolean payStatus,
            Boolean cancelStatus,
            Boolean refundStatus
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, 50, Sort.by(sorts));

        Page<Order> orderPage = orderService.search(rq.getMember(), payStatus, cancelStatus, refundStatus, pageable);

        rq.attr("orderPage", orderPage);

        return "domain/product/order/myList";
    }

    @GetMapping("/{id}")
    public String showDetail(@PathVariable long id, Model model) {
        Order order = orderService.findById(id).orElse(null);

        if (order == null) {
            throw new GlobalException("404", "존재하지 않는 주문입니다.");
        }

        Member actor = rq.getMember();

        long restCash = actor.getRestCash();

        if (!orderService.actorCanSee(actor, order)) {
            throw new GlobalException("403", "권한이 없습니다.");
        }

        model.addAttribute("order", order);
        model.addAttribute("actorRestCash", restCash);

        return "domain/product/order/detail";
    }

    @GetMapping("/success")
    public String showSuccess() {
        return "domain/product/order/success";
    }

    @GetMapping("/fail")
    public String showFail(String failCode, String failMessage) {
        rq.attr("code", failCode);
        rq.attr("message", failMessage);

        return "domain/product/order/fail";
    }

    @PostMapping("/{id}/payByCash")
    public String payByCash(@PathVariable long id) {
        Order order = orderService.findById(id).orElse(null);

        if (order == null) {
            throw new GlobalException("404", "존재하지 않는 주문입니다.");
        }

        if (!orderService.canPay(order, 0)) {
            throw new GlobalException("403", "권한이 없습니다.");
        }

        orderService.payByCashOnly(order);

        return rq.redirect("/order/" + order.getId(), "결제가 완료되었습니다.");
    }

    @PostMapping("/confirm")
    public ResponseEntity<JSONObject> confirmPayment2(@RequestBody String jsonBody) throws Exception {

        JSONParser parser = new JSONParser();
        String orderId;
        String amount;
        String paymentKey;
        try {
            // 클라이언트에서 받은 JSON 요청 바디입니다.
            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) requestData.get("paymentKey");
            orderId = (String) requestData.get("orderId");
            amount = (String) requestData.get("amount");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // 체크
        orderService.checkCanPay(orderId, Long.parseLong(amount));

        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

        // TODO: 개발자센터에 로그인해서 내 결제위젯 연동 키 > 시크릿 키를 입력하세요. 시크릿 키는 외부에 공개되면 안돼요.
        // @docs https://docs.tosspayments.com/reference/using-api/api-keys
        String apiKey = AppConfig.getTossPaymentsWidgetSecretKey();

        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
        // @docs https://docs.tosspayments.com/reference/using-api/authorization#%EC%9D%B8%EC%A6%9D
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((apiKey + ":").getBytes("UTF-8"));
        String authorizations = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        // 결제 승인 API를 호출하세요.
        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        // @docs https://docs.tosspayments.com/guides/payment-widget/integration#3-결제-승인하기
        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200 ? true : false;

        // 결제 승인이 완료
        if (isSuccess) {
            orderService.payByTossPayments(orderService.findByCode(orderId).get(), Long.parseLong(amount));
        } else {
            throw new RuntimeException("결제 승인 실패");
        }

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        // TODO: 결제 성공 및 실패 비즈니스 로직을 구현하세요.
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();

        return ResponseEntity.status(code).body(jsonObject);
    }
}