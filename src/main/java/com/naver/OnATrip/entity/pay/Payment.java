package com.naver.OnATrip.entity.pay;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private Long id;

//    private Orders orders;       // 해당 결제가 속한 주문

//    private String payName;     //주문명

//    private int payPrice;       //결제 가격

//    @ManyToOne
//    @JoinColumn(name = "item_id")
//    private Item item;              //상품

//    private LocalDateTime payDate;   //결제 시각

//    private PaymentStatus status;   //결제 상태



   // @Enumerated(EnumType.STRING)
   // private payMethod payMethod;        //결제방식

    @Column(length = 100, name = "merchant_uid")
    private String merchantUid;      //주문명

    private BigDecimal amount;  //총 가격

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime orderDate;



}
