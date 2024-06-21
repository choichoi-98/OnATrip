package com.naver.OnATrip.entity.plan;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.Plan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class DetailPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long planId; //-> 추후 수정 필요

    private String country;//->임시
    //추후에 국가 및 나라 코드 google api에 맞춰서 수정 필요!
    //locationId로 바꿔야 함.

    private String perDate; //날짜별
}
