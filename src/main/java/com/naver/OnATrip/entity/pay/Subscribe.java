package com.naver.OnATrip.entity.pay;

import com.naver.OnATrip.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 생성자를 통해서 값 변경 목적으로 접근하는 메시지들 차단
public class Subscribe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String memberId;

    @CreationTimestamp
    @Column(name = "start_date", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_DATE")
    private LocalDate startDate;

    private String endDate;

    private int itemPeriod;         //구독권 기간

    @Enumerated(EnumType.STRING)
    private SubscribeStatus status;     //현재 구독 여부

//    @Enumerated(EnumType.STRING)
//    private SubscribeRenewal renewal;


    @Builder
    public Subscribe(String memberId, String endDate, int itemPeriod, SubscribeStatus status) {
        this.memberId = memberId;
        this.endDate = endDate;
        this.itemPeriod = itemPeriod;
        this.status = status;
    }


//    //-- 구독권 결제시 구독 추가 --//
//    public static Subscribe createSubscribe(Member member, Pay pay) {
//        Subscribe subscribe = new Subscribe();
//        subscribe.setMember(member);
//
//        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDateTime startDate = LocalDate.parse(pay.getPayDate(), timeFormat);
//
//        Item item = pay.getItem();
//        LocalDateTime endDate = startDate.plusDays(item.getPeriod());
//
//        subscribe.setStartDate(startDate.toString());
//        subscribe.setEndDate(endDate.toString());
//
//        subscribe.setStatus(SubscribeStatus.ON);        //구독 상태 기본값 : 구독 ON
//        subscribe.setRenewal(SubscribeRenewal.RENEW);   //구독 연장 여부 : 연장 RENEW
//
//        return subscribe;



}
