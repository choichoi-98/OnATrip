package com.naver.OnATrip.web.dto.pay;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.pay.Item;
import com.naver.OnATrip.entity.pay.payMethod;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderDto {
    private Long id;

    private Member member;

    private List<Item> items = new ArrayList<>();   //주문 상품 리스트
    private String orderId;     //주문 아이디
    private int itemId;         //상품 번호

    payMethod payMethod;        //결제방식

    private String merchantUid; //주문 번호

    private BigDecimal totalPrice;  //가격

    private LocalDateTime orderDate;    //주문 시간

    private Boolean paymentStatus = false;  //결제상태




}
