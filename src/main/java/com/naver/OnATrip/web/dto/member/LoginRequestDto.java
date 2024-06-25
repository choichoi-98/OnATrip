package com.naver.OnATrip.web.dto.member;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {

    private String loginId;
    private String password;
}
