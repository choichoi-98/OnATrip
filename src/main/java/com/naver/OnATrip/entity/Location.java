package com.naver.OnATrip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Lob
    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] image;
}

