package com.naver.OnATrip.entity.pay;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.web.dto.pay.OrderDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "orders")
@NoArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;                //주문 번호

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id", nullable = false)
//    private Member member;          //주문한 회원

//    @ManyToMany
//    @JoinTable(
//            name = "order_items",
//            joinColumns = @JoinColumn(name = "order_id"),
//            inverseJoinColumns = @JoinColumn(name = "item_id")
//    )
//    private List<Item> items = new ArrayList<>();       //주문된 아이템 리스트

    @Enumerated(EnumType.STRING)
    private payMethod payMethod;        //결제방식

    @Column(length = 100, name = "merchant_uid")
    private String merchantUid;      //주문명

    private BigDecimal amount;  //총 가격

    private String orderUid; // 주문 번호

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime orderDate;    //주문 시간

    private Boolean paymentStatus = false;  //결제상태

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pay_id")
    private Payment payments;  //결제 내역

    public Orders(OrderDto request) {
    }

//
//    public void orderConfirm(String merchantUid, OrderDto orderDto) {
//        this.merchantUid = merchantUid;
//        this.totalPrice = orderDto.getTotalPrice();
//        this.payMethod = orderDto.getPayMethod();
//        this.orderDate = LocalDateTime.now();
//    }

    public void setPaymentStatus(Boolean paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Builder
    public Orders(Long id, String orderUid, /* List<Item> items, */ com.naver.OnATrip.entity.pay.payMethod payMethod, String merchantUid, BigDecimal amount, LocalDateTime orderDate, Boolean paymentStatus) {
        this.id = id;
//        this.member = member;
        this.orderUid = orderUid;
//        this.items = items;
        this.payMethod = payMethod;
        this.merchantUid = merchantUid;
        this.amount = amount;
        this.orderDate = orderDate;
        this.paymentStatus = paymentStatus;
    }
}
