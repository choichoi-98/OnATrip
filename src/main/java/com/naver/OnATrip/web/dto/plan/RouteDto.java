package com.naver.OnATrip.web.dto.plan;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.naver.OnATrip.entity.plan.DetailPlan;
import com.naver.OnATrip.entity.plan.Route;
import com.naver.OnATrip.constant.RouteCategory;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class RouteDto {

    private long id;

    @JsonProperty("detailPlanId")
    private Long detailPlan_id;

    @NotNull(message = "Day cannot be null")
    @JsonProperty("day")
    private int day_number;    //1일차, 2일차

    @NotNull(message = "Category cannot be null")
    private String category;


    private int routeSequence;//정렬 순서(day_number별 정렬)

    private String placeName;

    private String memo;

    private String address;

    private BigDecimal lat; //위도

    private BigDecimal lng; //경도

    private String sortKey; // 정렬 키

    @Builder
    public RouteDto(int day_number, String category, Long detailPlan_id, int routeSequence, String placeName, String memo, String address, BigDecimal lat, BigDecimal lng) {
        this.day_number = day_number;
        this.category = category;
        this.detailPlan_id = detailPlan_id;
        this.routeSequence = routeSequence;
        this.placeName = placeName;
        this.memo = memo;
        this.address = address;
        this.lat = lat;
        this.lng = lng;
    }



    //dto의 데이터 entity 클래스로 변환
    public Route toEntity() {
        return Route.builder()
                .detailPlan(DetailPlan.builder().id(detailPlan_id).build())
                .day_number(day_number)
                .category(RouteCategory.valueOf(category))
                .place_name(placeName)
                .memo(memo)
                .address(address)
                .routeSequence(routeSequence)
                .lat(lat)
                .lng(lng)
                .build();
    }

    //entity 클래스를 매개변수로 받는 생성자
    public RouteDto(Route route){
        this.id = route.getId();
        this.detailPlan_id = route.getDetailPlan().getId();
        this.day_number = route.getDay_number();
        this.category = String.valueOf(route.getCategory());
        this.placeName = route.getPlace_name();
        this.memo = route.getMemo();
        this.address = route.getAddress();
        this.routeSequence = route.getRouteSequence();
        this.lat = route.getLat();
        this.lng = route.getLng();
        this.sortKey = route.getSortKey();
    }

    @Override
    public String toString() {
        return "RouteDto{" +
                "id=" + id +
                ", detailPlan_id=" + detailPlan_id +
                ", day_number=" + day_number +
                ", category='" + category + '\'' +
                ", routeSequence=" + routeSequence +
                ", placeName='" + placeName + '\'' +
                ", memo='" + memo + '\'' +
                ", address='" + address + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", sortKey='" + sortKey + '\'' +
                '}';
    }
}