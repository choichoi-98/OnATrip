package com.naver.OnATrip.web.dto;

import com.naver.OnATrip.entity.Alert;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertDto {
    private Long id;
    private String memberEmail; // 이메일로 식별
    private String sourceMemberEmail; // 이메일로 식별
    private Long planId;
    private String message;
    private LocalDateTime createdAt;

    public static AlertDto fromEntity(Alert alert) {
        return AlertDto.builder()
                .id(alert.getId())
                .memberEmail(alert.getMemberEmail()) // 이메일로 가져오기
                .sourceMemberEmail(alert.getSourceMemberEmail()) // 이메일로 가져오기
                .planId(alert.getPlan() != null ? alert.getPlan().getId() : null)
                .message(alert.getMessage())
                .createdAt(alert.getCreatedAt())
                .build();
    }

    @Override
    public String toString() {
        return "AlertDto{" +
                "id=" + id +
                ", memberEmail='" + memberEmail + '\'' +
                ", sourceMemberEmail='" + sourceMemberEmail + '\'' +
                ", planId=" + planId +
                ", message='" + message + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}