package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.Location;
import com.naver.OnATrip.repository.admin.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }


    @Transactional
    //여행지 추가
    public void addLocation(String country, String countryCode, String city, String description, MultipartFile file, String locationType) throws IOException {
        // 파일 유효성 검사
        if (file.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일을 업로드해야 합니다.");
        }

        Location location = new Location();
        location.setCountryName(country);
        location.setCountryCode(countryCode);
        location.setCity(city);
        location.setDescription(description);
        location.setLocationType(locationType); // locationType 설정
        location.setImage(file.getBytes()); // 이미지를 byte 배열로 변환하여 설정

        // 여행지 저장
        locationRepository.save(location);
    }
}