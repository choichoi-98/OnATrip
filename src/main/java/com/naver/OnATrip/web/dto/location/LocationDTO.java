package com.naver.OnATrip.web.dto.location;

import com.naver.OnATrip.entity.Location;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

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
    private LocalDate createdDate;
    private LocalDate endDate;

    // 기본 생성자
    public LocationDTO() {
        // 기본 생성자에서 초기화 로직 제거
    }

    // Location 엔티티를 받아들이는 생성자
    public LocationDTO(Location location) {
        this.id = location.getId();
        this.countryName = location.getCountryName();
        this.countryCode = location.getCountryCode();
        this.city = location.getCity();
        this.description = location.getDescription();
        this.locationType = location.getLocationType();
        this.imagePath = location.getImage();
        this.createdDate = location.getCreatedDate();
        this.endDate = location.getEndDate(); // DB에서 가져온 endDate 그대로 사용
    }

    // toString() 메소드 오버라이드
    @Override
    public String toString() {
        return "LocationDTO{" +
                "id=" + id +
                ", countryName='" + countryName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                ", file=" + (file != null ? file.getOriginalFilename() : null) +
                ", locationType='" + locationType + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", createdDate=" + createdDate +
                ", endDate=" + endDate +
                '}';
    }
}