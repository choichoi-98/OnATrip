package com.naver.OnATrip.web.dto.location;

import com.naver.OnATrip.entity.Location;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class LocationDTO {
    private long id;
    private String countryName;
    private String countryCode;
    private String city;
    private String description;
    private MultipartFile file;
    private String locationType;
    private String imagePath;
    private LocalDate createdDate;
    private LocalDate endDate;

    // 기본 생성자
    public LocationDTO() {
        // 기본 생성자
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
        this.endDate = location.getEndDate();
    }

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