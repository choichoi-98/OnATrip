package com.naver.OnATrip.entity.plan;

import com.naver.OnATrip.constant.RouteCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DetailPlan detailPlan;

    @Enumerated(EnumType.STRING)
    private RouteCategory category; // 장소, 메모

    private int day_number; // 여행 일차(1일차, 2일차...)

    private String place_name; // 장소명

    private String memo; // 메모

    private int routeSequence; // 정렬 순서

    private String address;

    private int markSeq;

    //전체자리:18, 소수점 이하:15
    @Column(precision = 18, scale = 15)
    private BigDecimal lat; // 위도

    @Column(precision = 18, scale = 15)
    private BigDecimal lng; // 경도

    @Column(name = "sort_key")
    private String sortKey;
    private Route() {
    }

    public void updateRouteSequence(int routeSequence){
        this.routeSequence = routeSequence;
        this.updateSortKey();
    }

    @PrePersist
    @PreUpdate
    private void updateSortKey() {
        this.sortKey = String.format("%d-%03d", day_number, routeSequence);
        this.updateMarkSeq();
    }

    private void updateMarkSeq() {
        // category가 PLACE인 Route 엔티티들을 day_number와 routeSequence로 정렬
        List<Route> routesByDayAndSequence = this.detailPlan.getRoutes().stream()
                .filter(route -> route.getCategory() == RouteCategory.PLACE)
                .sorted(Comparator.comparing(Route::getDay_number)
                        .thenComparing(Route::getRouteSequence))
                .collect(Collectors.toList());

        // 정렬된 Route 엔티티들의 markSeq를 1부터 순서대로 업데이트
        for (int i = 0; i < routesByDayAndSequence.size(); i++) {
            routesByDayAndSequence.get(i).markSeq = i + 1;
        }
    }

    @Builder//setter제거했기 때문에 데이터를 넣기 위해 builder 패턴을 이용
    public Route(Long id, DetailPlan detailPlan, RouteCategory category, int day_number, String place_name, String memo, int routeSequence, String address, BigDecimal lat, BigDecimal lng, int markSeq) {
        this.detailPlan = detailPlan;
        this.category = category;
        this.day_number = day_number;
        this.place_name = place_name;
        this.memo = memo;
        this.routeSequence = routeSequence;
        this.updateSortKey();
        this.address = address;
        this.lat = lat;
        this.lng = lng;
        this.markSeq = markSeq;
    }
}