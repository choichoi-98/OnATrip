package com.naver.OnATrip.entity.plan;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@ToString
public class DetailPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detailPlan_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    private Long memberId;//회원가입 기능 완료 시 수정필요

    private String countryName;

    private String countryCode;

    private LocalDate perDate; //날짜별

    @OneToMany(mappedBy = "detailPlan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Route> routes = new ArrayList<>();

    protected DetailPlan() {
    }

    @Builder
    public DetailPlan( Long id,Plan plan, Long memberId, String countryName, String countryCode,  LocalDate perDate) {
        this.id = id;
        this.plan = plan;
        this.memberId = memberId;
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.perDate = perDate;
    }
}
