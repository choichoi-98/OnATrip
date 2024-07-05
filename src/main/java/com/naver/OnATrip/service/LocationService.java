package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.Location;
import com.naver.OnATrip.repository.admin.LocationRepository;
import com.naver.OnATrip.web.dto.location.LocationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;
    private static final String UPLOAD_DIR = "src/main/resources/static/images/location";

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    public void addLocation(LocationDTO locationDTO) throws IOException {
        // 파일 유효성 검사
        MultipartFile file = locationDTO.getFile();
        if (file.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일을 업로드해야 합니다.");
        }

        // 이미지 저장 경로 설정
        String fileName = file.getOriginalFilename();
        Path uploadPath = Paths.get("src/main/resources/static/images/location");

        // 디렉토리 존재하지 않으면 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 이미지 파일 저장
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, locationDTO.getFile().getBytes());

        // 이미지 경로 설정
        String imagePath = "/images/location/" + fileName;
        locationDTO.setImagePath(imagePath);

        // DTO를 엔티티로 변환
        Location location = new Location();
        location.setCountryName(locationDTO.getCountryName());
        location.setCountryCode(locationDTO.getCountryCode());
        location.setCity(locationDTO.getCity());
        location.setDescription(locationDTO.getDescription());
        location.setLocationType(locationDTO.getLocationType());
        location.setImage(imagePath);

        // 여행지 저장
        locationRepository.save(location);
    }

    // 도시 중복 검사
    public boolean existsCity(String cityName) {
        return locationRepository.existsByCity(cityName);
    }

    // 국가 중복 검사
    public boolean existsbyName(String countryName) {
        return locationRepository.existsByCountryName(countryName);
    }

    // 여행지 목록
    public List<LocationDTO> getAllLocations() {
        List<Location> locations = locationRepository.findAll();
        List<LocationDTO> locationDTOs = new ArrayList<>();

        for (Location location : locations) {
            LocationDTO locationDTO = new LocationDTO();
            locationDTO.setCountryName(location.getCountryName());
            locationDTO.setCountryCode(location.getCountryCode());
            locationDTO.setCity(location.getCity());
            locationDTO.setDescription(location.getDescription());
            locationDTO.setLocationType(location.getLocationType());
            locationDTO.setImagePath(location.getImage()); // 필드 이름 변경
            locationDTOs.add(locationDTO);
        }

        return locationDTOs;
    }
}
