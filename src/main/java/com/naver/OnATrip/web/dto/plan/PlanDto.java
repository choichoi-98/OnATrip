package com.naver.OnATrip.web.dto.plan;

import com.naver.OnATrip.entity.plan.Plan;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlanDto {

    private long id;

    private Long memberId;

    private String country;//-> 임시

    private String startDate;

    private String endDate;

    private Long mateId;


    //dto의 데이터를 entity클래스로 변환
    public Plan toEntity(){
        return Plan.builder()
//                .id(id)
                .memberId(memberId)
                .country(country)
                .startDate(startDate)
                .endDate(endDate)
                .mateId(mateId)
                .build();

    }

    //entity 클래스를 매개변수로 받는 생성자
    public PlanDto(Plan plan) {
        this.id = plan.getId();
        this.memberId = plan.getMemberId();
        this.country = plan.getCountry();
        this.startDate = plan.getStartDate();
        this.endDate = plan.getEndDate();
        this.mateId = plan.getMateId();
    }
}
