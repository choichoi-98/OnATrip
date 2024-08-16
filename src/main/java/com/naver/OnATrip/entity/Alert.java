package com.naver.OnATrip.entity;

import com.naver.OnATrip.entity.plan.Plan;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ALERT")
public class Alert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memberEmail;  // 이메일로 식별

    @Column(nullable = false)
    private String sourceMemberEmail;  // 이메일로 식별

    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = true)
    private Plan plan;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Alert(String memberEmail, String sourceMemberEmail, Plan plan, String message) {
        this.memberEmail = memberEmail;
        this.sourceMemberEmail = sourceMemberEmail;
        this.plan = plan;
        this.message = message;
        this.createdAt = LocalDateTime.now(); // 현재 시간으로 설정
    }
}