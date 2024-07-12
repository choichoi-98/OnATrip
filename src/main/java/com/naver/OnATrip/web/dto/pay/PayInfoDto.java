package com.naver.OnATrip.web.dto.pay;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class PayInfoDto {

    private BigDecimal amount;
    private String itemId;
    private String merchant_uid;
    private String imp_uid;
}
