package com.naver.OnATrip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Location {


    @Id
    @Column(name = "location_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 생성 전략 설정
    private Long id;

    @Column(name = "location_type")
    private String locationType;

    private String countryName;

    private String countryCode;

    private String city;

    private String description;

    private String image;

    @Column(name = "created_date")
    private LocalDate createdDate = LocalDate.now(); // 현재 날짜로 초기화

    private LocalDate endDate;

}

