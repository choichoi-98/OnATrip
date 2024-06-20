package com.naver.OnATrip.entity.pay;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter @Setter
public class Pay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "member_id", insertable = false, updatable = false) //MEMBER_ID를 읽기 전용으로만 사용
    private String memberId;

    private String payName; //주문명

    private int payPrice;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private String pgToken;

    @Column(name = "pay_date")
    private LocalDateTime payDate;   //결제 날짜


}
