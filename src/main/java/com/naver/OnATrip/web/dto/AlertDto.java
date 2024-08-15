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
    private Long memberId;
    private Long sourceMemberId;
    private String message;
    private LocalDateTime createdAt;

    public static AlertDto fromEntity(Alert alert) {
        return AlertDto.builder()
                .id(alert.getId())
                .memberId(alert.getMember().getId())
                .sourceMemberId(alert.getSourceMember().getId())
                .message(alert.getMessage())
                .createdAt(alert.getCreatedAt())
                .build();
    }
}
