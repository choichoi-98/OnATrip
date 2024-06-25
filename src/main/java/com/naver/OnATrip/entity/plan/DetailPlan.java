package com.naver.OnATrip.entity.plan;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class DetailPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    private Long memberId;//회원가입 기능 완료 시 수정필요

    private String country;//->임시
    //추후에 국가 및 나라 코드 google api에 맞춰서 수정 필요!
    //locationId로 바꿔야 함.

    private LocalDate perDate; //날짜별
}
