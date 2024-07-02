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
        Path uploadPath = Paths.get(UPLOAD_DIR + locationDTO.getCountryCode());

        // 디렉토리 존재하지 않으면 생성
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // 이미지 파일 저장
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        // 이미지 경로 설정
        String imagePath = "/images/" + locationDTO.getCountryCode() + "/" + fileName;

        // DTO를 엔티티로 변환
        Location location = new Location();
        location.setCountryName(locationDTO.getCountry());
        location.setCountryCode(locationDTO.getCountryCode());
        location.setCity(locationDTO.getCity());
        location.setDescription(locationDTO.getDescription());
        location.setLocationType(locationDTO.getLocationType());
        location.setImage(imagePath);

        // 여행지 저장
        locationRepository.save(location);
    }
}
