package com.naver.OnATrip.web.dto.location;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

// LocationDTO
@Getter
@Setter
public class LocationDTO {
    private long id;
    private String countryName;
    private String countryCode;
    private String city;
    private String description;
    private MultipartFile file; // 실제 파일 객체
    private String locationType;
    private String imagePath; // 필드 이름 변경

    // 기본 생성자
    public LocationDTO() {
        
    }
}