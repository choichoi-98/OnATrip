package com.naver.OnATrip.entity.plan;

import com.naver.OnATrip.constant.RouteCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "detailPlan_id")
    private DetailPlan detailPlan;

    @Enumerated(EnumType.STRING)
    private RouteCategory category;//장소,메모

    private int day_number;//여행 일차(1일차, 2일차...)

    private String place_name;//장소명

    private String memo;//메모

    private String address;

    private String lat;//위도

    private String lon;//경도

}
