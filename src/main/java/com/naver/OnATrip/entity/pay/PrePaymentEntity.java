package com.naver.OnATrip.entity.pay;

import com.naver.OnATrip.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class PrePaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;        //주문 번호

    private String merchantUid;      //주문명

    private BigDecimal amount;  //총 가격


}
