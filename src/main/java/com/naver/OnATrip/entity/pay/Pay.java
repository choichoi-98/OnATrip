package com.naver.OnATrip.entity.pay;

import com.naver.OnATrip.entity.Member;
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
    @Column(name = "pay_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;       // 해당 결제가 속한 주문

    private String payName;     //주문명

    private int payPrice;       //결제 가격

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;              //상품

    private LocalDateTime payDate;   //결제 시각

    private Boolean status = true; // 상태

    public Member getMember() {
        if (orders != null) {
            return orders.getMember();
        }
        return null;
    }


}
