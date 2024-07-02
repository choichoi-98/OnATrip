package com.naver.OnATrip.entity.plan;

import com.naver.OnATrip.constant.RouteCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//"PROTECTED"-> 기본생성자로 객체 생성하는 것 막음.
@Entity
@Getter
@ToString
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "detailPlan_id")
    private DetailPlan detailPlan;

    @Enumerated(EnumType.STRING)
    private RouteCategory category; // 장소, 메모

    private int day_number; // 여행 일차(1일차, 2일차...)

    private String place_name; // 장소명

    private String memo; // 메모

    private int routeSequence; // 정렬 순서

    private String address;

    //전체자리:18, 소수점 이하:15
    @Column(precision = 18, scale = 15)
    private BigDecimal lat; // 위도

    @Column(precision = 18, scale = 15)
    private BigDecimal lng; // 경도

    @Column(name = "sort_key")
    private String sortKey;
    private Route() {
    }

    @PrePersist
    @PreUpdate
    private void updateSortKey() {
        this.sortKey = String.format("%d-%03d", day_number, routeSequence);
    }

    @Builder//setter제거했기 때문에 데이터를 넣기 위해 builder 패턴을 이용
    public Route(Long id, DetailPlan detailPlan, RouteCategory category, int day_number, String place_name, String memo, int routeSequence, String address, BigDecimal lat, BigDecimal lng) {
//        this.id = id;
        this.detailPlan = detailPlan;
        this.category = category;
        this.day_number = day_number;
        this.place_name = place_name;
        this.memo = memo;
        this.routeSequence = routeSequence;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }
}