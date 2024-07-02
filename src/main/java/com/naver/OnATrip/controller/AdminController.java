package com.naver.OnATrip.controller;

import com.naver.OnATrip.service.LocationService;
import com.naver.OnATrip.web.dto.location.LocationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


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

    @PostMapping("/admin/addLocation")
    public String addLocation(LocationDTO locationDTO) throws IOException {

        logger.info("Received request to add location with country={}, countryCode={}, city={}, description={}, locationType={}",
                locationDTO.getCountry(), locationDTO.getCountryCode(), locationDTO.getCity(), locationDTO.getDescription(), locationDTO.getLocationType());

        // 파일 유효성 검사
        if (locationDTO.getFile().isEmpty()) {
            throw new IllegalArgumentException("이미지 파일을 업로드해야 합니다.");
        }

        // 위치 정보 추가
        locationService.addLocation(locationDTO);

        return "redirect:/admin"; // 추가 성공 시 관리자 페이지로 리다이렉트
    }
}