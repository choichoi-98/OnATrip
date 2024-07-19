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

    private String email;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private String startDate;

    private String endDate;

    private String mateId;

    private int memberCount;

    @OneToMany(mappedBy = "plan",cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<DetailPlan> detailPlans = new ArrayList<>();

    protected Plan() {
    }

    public void incrementMemberCount(){
        this.memberCount++;
    }


    public void updateMateId(String mateId) {
        this.mateId = mateId;
    }

    @Builder
    public Plan(Long id, String email, Location location, String startDate, String endDate, String mateId, int memberCount) {
        this.id = id;
        this.email = email;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.mateId = mateId;
        this.memberCount = memberCount == 0 ? 1 : memberCount;//기본값을 1로 설정
    }

    public static Plan from(LocationProjection locationProjection, PlanDto requestDto) {
        Location location = new Location();
        location.setId(locationProjection.getId());
        location.setLocationType(locationProjection.getLocationType());
        location.setCountryName(locationProjection.getCountryName());
        location.setCountryCode(locationProjection.getCountryCode());
        location.setImage(locationProjection.getImage());

        return Plan.builder()
                .id(requestDto.getId())
                .email(requestDto.getEmail())
                .location(location)
                .startDate(requestDto.getStartDate())
                .endDate(requestDto.getEndDate())
                .mateId(requestDto.getMateId())
                .memberCount(requestDto.getMemberCount())
                .build();
    }


}