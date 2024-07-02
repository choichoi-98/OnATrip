package com.naver.OnATrip.web.dto.location;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class LocationDTO {
    private String country;
    private String countryCode;
    private String city;
    private String description;
    private MultipartFile file;
    private String locationType;
}