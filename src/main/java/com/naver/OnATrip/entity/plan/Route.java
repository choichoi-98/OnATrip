package com.naver.OnATrip.entity.plan;

import com.naver.OnATrip.constant.RouteCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "detailPlan_id")
    private DetailPlan detailPlan_id;

    @Enumerated(EnumType.STRING)
    private RouteCategory category; // 장소, 메모

    private int day_number; // 여행 일차(1일차, 2일차...)

    private String place_name; // 장소명

    private String memo; // 메모

    private String address;

    private String lat; // 위도

    private String lon; // 경도

    public Route(Long id, DetailPlan detailPlan_id, RouteCategory category, int day_number, String place_name, String memo, String address, String lat, String lon) {
        this.id = id;
        this.detailPlan_id = detailPlan_id;
        this.category = category;
        this.day_number = day_number;
        this.place_name = place_name;
        this.memo = memo;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }

    public Route() {
    }
}
