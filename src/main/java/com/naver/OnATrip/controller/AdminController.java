package com.naver.OnATrip.controller;

import com.naver.OnATrip.service.LocationService;
import com.naver.OnATrip.web.dto.location.LocationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;


@Controller
public class AdminController {

    private final LocationService locationService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    public AdminController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        return "admin/adminMain";
    }

    //여행지 추가
    @PostMapping("/admin/addLocation")
    public String addLocation(LocationDTO locationDTO) throws IOException {

        logger.info("Received request to add location with country={}, countryCode={}, city={}, description={}, locationType={}",
                locationDTO.getCountryName(), locationDTO.getCountryCode(), locationDTO.getCity(), locationDTO.getDescription(), locationDTO.getLocationType());

        // 파일 유효성 검사
        if (locationDTO.getFile().isEmpty()) {
            throw new IllegalArgumentException("이미지 파일을 업로드해야 합니다.");
        }

        // 위치 정보 추가
        locationService.addLocation(locationDTO);

        return "redirect:/admin"; // 추가 성공 시 관리자 페이지로 리다이렉트
    }

    // 도시 중복검사
    @PostMapping("/admin/checkCity")
    @ResponseBody
    public Map<String, Boolean> checkCity(@RequestParam("cityName") String cityName) {
        Map<String, Boolean> response = new HashMap<>();
        boolean exists = locationService.existsCity(cityName); // LocationService에서 도시 존재 여부 확인
        response.put("exists", exists);
        return response;
    }

    // 국가 중복검사
    @PostMapping("/admin/checkCountry")
    @ResponseBody
    public ResponseEntity<?> checkCountryExistence(@RequestParam("countryName") String countryName) {

     /*   logger.info("checkCountryExistence- controller");
        logger.info("country name : ", countryName);*/

        boolean exists = locationService.existsbyName(countryName);
        return ResponseEntity.ok(Collections.singletonMap("exists", exists));
    }

    // 여행지 목록
    @GetMapping("/admin/getAllLocations")
    @ResponseBody
    public List<LocationDTO> getLocations() {
        return locationService.getAllLocations();
    }

    // 여행지 수정
    @PostMapping("/updateLocation")
    @ResponseBody
    public String updateLocation(@ModelAttribute("locationDTO") LocationDTO locationDTO) throws IOException {
        logger.info("Received request to update location with id={}, country={}, countryCode={}, city={}, description={}, locationType={}, imagePath={}",
                locationDTO.getId(), locationDTO.getCountryName(), locationDTO.getCountryCode(), locationDTO.getCity(), locationDTO.getDescription(), locationDTO.getLocationType(), locationDTO.getImagePath());

        if (!locationDTO.getFile().isEmpty()) {
            // 새로운 이미지 파일이 업로드된 경우 처리
            locationService.updateLocationWithImage(locationDTO);
        } else {
            // 이미지 파일이 업로드되지 않은 경우 처리
            locationService.updateLocationWithoutImage(locationDTO);
        }

        return "redirect:/admin"; // 수정 성공 시 관리자 페이지로 리다이렉트
    }


}