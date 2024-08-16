package com.naver.OnATrip.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor //추가한 부분
@AllArgsConstructor //추가한 부분
@Builder //추가한 부분
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

    @Column(name = "end_date")
    private LocalDate endDate;

    @PrePersist
    public void prePersist() {
        this.createdDate = (this.createdDate == null) ? LocalDate.now() : this.createdDate;
        this.endDate = (this.endDate == null) ? this.createdDate.plusDays(5) : this.endDate;

        System.out.println("Created Date: " + this.createdDate);
        System.out.println("End Date: " + this.endDate);
    }

}

