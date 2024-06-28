package com.naver.OnATrip.web.dto.plan;

import com.naver.OnATrip.entity.plan.DetailPlan;
import com.naver.OnATrip.entity.plan.Route;
import com.naver.OnATrip.constant.RouteCategory;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RouteDto {

    private long id;

    @NotNull(message = "Day cannot be null")
    private int day;    //1일차, 2일차

    @NotNull(message = "Category cannot be null")
    private String category;

    private Long detailPlan_id;

    private int day_number;

    private String placeName;

    private String memo;

    private String address;

    private String lat;

    private String lon;

    public RouteDto() {
        // 기본 생성자 추가
    }

    public RouteDto(Long id, int day, String category, int day_number, Long detailPlan_id, String placeName, String memo, String address, String lat, String lon) {
        this.id = id;
        this.day = day;
        this.category = category;
        this.detailPlan_id = detailPlan_id;
        this.day_number = day_number;
        this.placeName = placeName;
        this.memo = memo;
        this.address = address;
        this.lat = lat;
        this.lon = lon;
    }

    public Route toEntity(DetailPlan detailPlan) {
        return Route.builder()
                .id(this.id)
                .day_number(this.day_number)
                .detailPlan_id(detailPlan)
                .category(RouteCategory.valueOf(this.category))
                .place_name(this.placeName)
                .memo(this.memo)
                .address(this.address)
                .lat(this.lat)
                .lon(this.lon)
                .build();
    }

    public static RouteDto fromEntity(Route route) {
        RouteDto dto = new RouteDto();
        dto.setId(route.getId());
        dto.setDay(route.getDay_number());  // 여기서는 day와 day_number가 혼동되어 사용됐습니다. 수정 필요
        dto.setCategory(route.getCategory().name());
        dto.setDetailPlan_id(route.getDetailPlan_id().getId());  // DetailPlan의 id로 설정
        dto.setDay_number(route.getDay_number());
        dto.setPlaceName(route.getPlace_name());
        dto.setMemo(route.getMemo());
        dto.setAddress(route.getAddress());
        dto.setLat(route.getLat());
        dto.setLon(route.getLon());
        return dto;
    }
}
