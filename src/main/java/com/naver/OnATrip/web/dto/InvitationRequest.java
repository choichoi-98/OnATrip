package com.naver.OnATrip.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitationRequest {
    @NotBlank
    private String email;
    @NotNull
    private Long planId;
}
