package com.naver.OnATrip.web.dto.pay;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemDto {
    private int itemId;                 //상품 번호

    private String name;            //상품명

    private int itemPrice;          //상품 가격

    private int period;              //구독 기간
}
