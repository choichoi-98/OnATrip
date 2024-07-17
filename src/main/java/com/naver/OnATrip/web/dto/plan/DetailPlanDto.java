package com.naver.OnATrip.web.dto.plan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.naver.OnATrip.entity.plan.DetailPlan;
import com.naver.OnATrip.entity.plan.Plan;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DetailPlanDto {

    private Long id;

    @JsonProperty("planId")
    private Long plan_id;

    private Long memberId;//회원가입 기능 완료 시 수정필요

    private String countryName;

    private String countryCode;

    private LocalDate perDate;

    @Builder
    public DetailPlanDto(Long planId, Long memberId, String countryName, String countryCode, LocalDate perDate) {
        this.plan_id = planId;
        this.memberId = memberId;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.perDate = perDate;
    }

    //dto의 데이터 entity 클래스로 변환
    public DetailPlan toEntity(){

        return DetailPlan.builder()
                .plan(Plan.builder().id(plan_id).build()) // Plan 엔티티의 ID만 설정
                .memberId(memberId)
                .countryName(countryName)
                .countryCode(countryCode)
                .perDate(perDate)
                .build();

    }

    //entity클래스를 매개변수로 받는 생성자
    public DetailPlanDto(DetailPlan detailPlan) {
        this.id = detailPlan.getId();
        this.plan_id = detailPlan.getPlan().getId();
        this.memberId = detailPlan.getMemberId();
        this.countryName = detailPlan.getCountryName();
        this.countryCode = detailPlan.getCountryCode();
        this.perDate = detailPlan.getPerDate();
    }
}
