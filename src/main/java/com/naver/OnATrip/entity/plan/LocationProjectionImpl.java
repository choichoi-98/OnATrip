package com.naver.OnATrip.entity.plan;

public class LocationProjectionImpl implements LocationProjection {

    private Long id;
    private String countryName;
    private String countryCode;
    private String image;

    // 기본 생성자
    public LocationProjectionImpl() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getCountryName() {
        return countryName;
    }

    @Override
    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public String getImage() {
        return image;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public void setImage(String image) {
        this.image = image;
    }
}