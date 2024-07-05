package com.naver.OnATrip.entity.plan;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@ToString
public class Plan {

    @Id
    @Column(name = "plan_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "member_id")
    private Long memberId;//회원가입 기능 완료 시 수정필요

//    @ManyToOne
//    @JoinColumn(name = "location_id")
    private String country;//->임시
    //추후에 국가 및 나라 코드 google api에 맞춰서 수정 필요!
    //locationId로 바꿔야 함.

    private String startDate;

    private String endDate;

    private Long mateId;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailPlan> detailPlans = new ArrayList<>();

    protected Plan() {
    }

    @Builder
    public Plan(Long id, Long memberId, String country, String startDate, String endDate, Long mateId) {
        this.id = id;
        this.memberId = memberId;
        this.country = country;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mateId = mateId;
    }
}
