package com.naver.OnATrip.entity;

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

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;  // 알림을 받을 회원

    @ManyToOne
    @JoinColumn(name = "source_member_id", nullable = false)
    private Member sourceMember;  // 알림을 발생시킨 회원

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Alert(Member member, Member sourceMember, String message) {
        this.member = member;
        this.sourceMember = sourceMember;
        this.message = message;
        this.createdAt = LocalDateTime.now();
    }
}
