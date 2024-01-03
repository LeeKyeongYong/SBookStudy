package com.multibook.bookorder.domain.product.order.entity;

import com.multibook.bookorder.domain.global.exceptions.GlobalException;
import com.multibook.bookorder.domain.member.member.entity.Member;
import com.multibook.bookorder.domain.product.cart.entity.CartItem;
import com.multibook.bookorder.domain.product.product.entity.Product;
import com.multibook.bookorder.global.app.AppConfig;
import com.multibook.bookorder.global.jpa.BaseTime;
import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.ALL;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@QueryEntity
@ToString(callSuper = true)
@Table(name = "order_")
public class Order extends BaseTime {
    @ManyToOne
    private Member buyer;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime payDate; // 결제일
    private LocalDateTime cancelDate; // 취소일
    private LocalDateTime refundDate; // 환불일

    public void addItem(CartItem cartItem) {
        addItem(cartItem.getProduct());
    }

    public void addItem(Product product) {
        if (buyer.has(product))
            throw new GlobalException("400-1", "이미 구매한 상품입니다.");

        OrderItem orderItem = OrderItem.builder()
                .order(this)
                .product(product)
                .build();

        orderItems.add(orderItem);
    }

    public long calcPayPrice() {
        return orderItems.stream()
                .mapToLong(OrderItem::getPayPrice)
                .sum();
    }

    public void setPaymentDone() {
        payDate = LocalDateTime.now();

        orderItems.stream()
                .forEach(OrderItem::setPaymentDone);
    }

    public void setCancelDone() {
        cancelDate = LocalDateTime.now();

        orderItems.stream()
                .forEach(OrderItem::setCancelDone);
    }

    public void setRefundDone() {
        refundDate = LocalDateTime.now();

        orderItems.stream()
                .forEach(OrderItem::setRefundDone);
    }

    public String getName() {
        String name = orderItems.get(0).getProduct().getName();

        if (orderItems.size() > 1) {
            name += " 외 %d건".formatted(orderItems.size() - 1);
        }

        return name;
    }

    public String getCode() {
        // yyyy-MM-dd 형식의 DateTimeFormatter 생성
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // LocalDateTime 객체를 문자열로 변환
        return getCreateDate().format(formatter) + (AppConfig.isNotProd() ? "-test-" + UUID.randomUUID().toString() : "") + "__" + getId();
    }

    public boolean isPayable() {
        if (payDate != null) return false;
        if (cancelDate != null) return false;

        return true;
    }

    public String getForPrintPayStatus() {
        if (payDate != null)
            return "결제완료(" + payDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";

        if (cancelDate != null) return "-";

        return "결제대기";
    }

    public String getForPrintCancelStatus() {
        if (cancelDate != null)
            return "취소완료(" + cancelDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";

        if (!isCancelable()) return "취소불가능";

        return "취소가능";
    }

    public String getForPrintRefundStatus() {
        if (refundDate != null)
            return "환불완료(" + refundDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + ")";

        if (payDate == null) return "-";
        if (!isCancelable()) return "-";

        return "환불가능";
    }

    public boolean isPayDone() {
        return payDate != null;
    }

    public boolean isCancelable() {
        if (cancelDate != null) return false;

        // 결제일자로부터 1시간 지나면 취소 불가능
        if (payDate != null && payDate.plusHours(1).isBefore(LocalDateTime.now())) return false;

        return true;
    }
}