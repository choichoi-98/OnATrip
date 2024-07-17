package com.naver.OnATrip.web.dto.pay;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class PaymentDto {
    private Long id;    //주문 PK번호

//    private Long memberId;  //주문한 회원 ID

    private int itemId;         //상품 번호

    private String payMethod;        //결제방식

    private String merchantUid; //주문 번호

    private BigDecimal amount;  //가격

    private LocalDateTime orderDate;    //주문 시간

    private Boolean paymentStatus = false;  //결제상태

    private String impUid;
}
