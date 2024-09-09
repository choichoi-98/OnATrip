package com.naver.OnATrip.web.dto.pay;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.pay.SubscribeStatus;
import lombok.Data;

@Data
public class SubscribeDto {

    private Long id;
    private String memberId;
    private int itemId;
    private String startDate;
    private String renewal;
    private SubscribeStatus status;

//    public SubscribeDto(Long id, Member memberId, String startDate, String renewal, SubscribeStatus status) {
//        this.id = id;
//        this.memberId = memberId;
//        this.startDate = startDate;
//        this.renewal = renewal;
//        this.status = status;
//    }

}
