package com.naver.OnATrip.constant;

public enum RouteCategory {
    PLACE("p"), //장소
    MEMO("m");  //메모

    private final String code;

    RouteCategory(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
