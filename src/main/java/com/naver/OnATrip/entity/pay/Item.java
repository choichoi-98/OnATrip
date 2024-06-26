package com.naver.OnATrip.entity.pay;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int id;                 //상품 번호

    @Column(name = "item_name")
    private String name;            //상품명

    @Column(name = "item_price")
    private int itemPrice;          //상품 가격

    @Column(name = "item_period")
    private int period;              //구독 기간
}
