package com.naver.OnATrip.web.dto.pay;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.pay.Item;
import com.naver.OnATrip.entity.pay.payMethod;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class OrderDto {
    private Long id;    //주문 PK번호

//    private Long memberId;  //주문한 회원 ID

//    private List<Item> items = new ArrayList<>();   //주문 상품 리스트

    private int itemId;         //상품 번호

    private int itemPeriod;

    private String payMethod;        //결제방식

    private String merchantUid; //주문 번호

    private BigDecimal amount;  //가격

    private LocalDateTime orderDate;    //주문 시간

    private Boolean paymentStatus = false;  //결제상태

    private String impUid;





}
