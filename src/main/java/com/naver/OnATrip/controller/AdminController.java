package com.naver.OnATrip.controller;

import com.naver.OnATrip.service.LocationService;
import com.naver.OnATrip.web.dto.location.LocationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final LocationService locationService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    public String admin(Model model) {
        return "admin/adminMain";
    }

    // 여행지 추가
    @PostMapping("/addLocation")
    public String addLocation(LocationDTO locationDTO) throws IOException {
        logger.info("Received request to add location with country={}, countryCode={}, city={}, description={}, locationType={}", locationDTO.getCountryName(), locationDTO.getCountryCode(), locationDTO.getCity(), locationDTO.getDescription(), locationDTO.getLocationType());

        // 파일 유효성 검사
        if (locationDTO.getFile().isEmpty()) {
            throw new IllegalArgumentException("이미지 파일을 업로드해야 합니다.");
        }

        // 위치 정보 추가
        locationService.addLocation(locationDTO);

        return "redirect:/admin"; // 추가 성공 시 관리자 페이지로 리다이렉트
    }

    // 도시 중복검사
    @PostMapping("/checkCity")
    @ResponseBody
    public Map<String, Boolean> checkCity(@RequestParam("cityName") String cityName) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = locationService.existsCity(cityName); // LocationService에서 도시 존재 여부 확인
        response.put("exists", exists);
        return response;
    }

    // 국가 중복검사
    @PostMapping("/checkCountry")
    @ResponseBody
    public ResponseEntity<?> checkCountryExistence(@RequestParam("countryName") String countryName) {
        boolean exists = locationService.existsbyName(countryName);
        return ResponseEntity.ok(Collections.singletonMap("exists", exists));
    }

    // 여행지 목록
    @GetMapping("/getAllLocations")
    @ResponseBody
    public List<LocationDTO> getLocations() {
        List<LocationDTO> locations = locationService.getAllLocations();
        return locations;
    }

    // 여행지 수정
    @PostMapping("/updateLocation")
    public String updateLocation(@ModelAttribute LocationDTO locationDTO) throws IOException {
        logger.info("Received request to update location with id={}, country={}, countryCode={}, city={}, description={}, locationType={}, imagePath={}", locationDTO.getId(), locationDTO.getCountryName(), locationDTO.getCountryCode(), locationDTO.getCity(), locationDTO.getDescription(), locationDTO.getLocationType(), locationDTO.getImagePath());

        // 이미지 파일 유무 확인
        MultipartFile file = locationDTO.getFile();
        if (file != null && !file.isEmpty()) {
            logger.info("Updating location with image");
            locationService.updateLocationWithImage(locationDTO); // 이미지 업로드된 경우
        } else {
            logger.info("Updating location without image");
            // 이미지 업로드되지 않은 경우, 기존 이미지와 위치 유형 유지하도록 설정
            LocationDTO existingLocation = locationService.getLocationById(locationDTO.getId());
            locationDTO.setImagePath(existingLocation.getImagePath()); // 기존 이미지로 설정
            locationDTO.setLocationType(existingLocation.getLocationType()); // 기존 위치 유형으로 설정

            locationService.updateLocationWithoutImage(locationDTO);
        }

        return "redirect:/admin";
    }

    // 여행지 삭제
    @DeleteMapping("/deleteLocation/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable("id") Long id) {
        LocationDTO location = locationService.getLocationById(id);
        if (location == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Location not found");
        }

        // 이미지 파일 삭제
        if (location.getImagePath() != null) {
            boolean imageDeleted = locationService.deleteImage(location.getImagePath());
            if (!imageDeleted) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete image file");
            }
        }

        // 위치 정보 삭제
        boolean deleted = locationService.deleteLocation(id);
        if (deleted) {
            return ResponseEntity.ok("Location deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete location");
        }
    }
}


