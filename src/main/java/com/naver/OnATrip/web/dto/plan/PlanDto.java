package com.naver.OnATrip.web.dto.plan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.naver.OnATrip.entity.Location;
import com.naver.OnATrip.entity.plan.LocationProjection;
import com.naver.OnATrip.entity.plan.Plan;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlanDto {

    private Long id;
    private String email;
    @JsonProperty("locationId")
    private Long locationId;
    private String startDate;
    private String endDate;
    private String mateId;
    private int memberCount;

    public Plan toEntity(LocationProjection locationProjection) {
        return Plan.from(locationProjection, this);
    }

    @Builder
    public PlanDto(Plan plan) {
        this.id = plan.getId();
        this.email = plan.getEmail();
        this.locationId = plan.getLocation().getId();
        this.startDate = plan.getStartDate();
        this.endDate = plan.getEndDate();
        this.mateId = plan.getMateId();
        this.memberCount = plan.getMemberCount();
    }
}
