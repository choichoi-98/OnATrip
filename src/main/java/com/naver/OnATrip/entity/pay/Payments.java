package com.naver.OnATrip.entity.pay;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Payments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private Long id;

//    private Orders orders;       // 해당 결제가 속한 주문

//    private String payName;     //주문명

    private int payPrice;       //결제 가격

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;              //상품

    private LocalDateTime payDate;   //결제 시각

    private PaymentStatus status;   //결제 상태

    private String paymentUid;  //결제 고유 번호

//    public Member getMember() {
//        if (orders != null) {
//            return orders.getMember();
//        }
//        return null;
//    }

    @Builder
    public Payments(int payPrice, PaymentStatus status){
        this.payPrice = payPrice;
        this.status = status;
    }

    public void changePaymentBySuccess(PaymentStatus status, String paymentUid){
        this.status = status;
        this.paymentUid = paymentUid;
    }


}
