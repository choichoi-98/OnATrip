package com.naver.OnATrip.constant;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

   Role(String value) {
        this.value = value;
    }

    private String value;
}
