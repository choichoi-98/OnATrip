package com.naver.OnATrip.entity.plan;

import com.naver.OnATrip.entity.Location;
import com.naver.OnATrip.web.dto.plan.PlanDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
public class Plan {

    @Id
    @Column(name = "plan_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private String startDate;

    private String endDate;

    private Long mateId;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetailPlan> detailPlans = new ArrayList<>();

    protected Plan() {
    }

    @Builder
    public Plan(Long id, Long memberId, Location location, String startDate, String endDate, Long mateId) {
        this.id = id;
        this.memberId = memberId;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mateId = mateId;
    }

    public static Plan from(LocationProjection locationProjection, PlanDto requestDto) {
        Location location = new Location();
        location.setId(locationProjection.getId());
        location.setCountryName(locationProjection.getCountryName());
        location.setCountryCode(locationProjection.getCountryCode());
        location.setImage(locationProjection.getImage());

        return Plan.builder()
                .id(requestDto.getId())
                .memberId(requestDto.getMemberId())
                .location(location)
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .mateId(requestDto.getMateId())
                .build();
    }

}