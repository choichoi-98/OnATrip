package com.naver.OnATrip.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Location {


    @Id
    @Column(name = "location_id")
    private Long id;

    private String countryName;

    private String countryCode;

    private String city;

    private String description;

    @Lob
    @Column(name = "image", columnDefinition = "BLOB")
    private byte[] image;
}