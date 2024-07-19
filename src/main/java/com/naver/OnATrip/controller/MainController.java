package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Location;
import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.service.LocationService;
import com.naver.OnATrip.web.dto.location.LocationDTO;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final LocationService locationService;

    public MainController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/main")
    // 메인 주의 여기 주의 주의 주의 주의
    public String main(Model model, HttpSession session) {
        logger.info("main---------------------------------------------------------");
        List<LocationDTO> allLocations = locationService.getAllLocations();
        model.addAttribute("allLocations", allLocations);
        logger.info("Printing all locations: {}", allLocations);

        List<LocationDTO> domesticLocations = locationService.getDomesticLocations();
        model.addAttribute("domesticLocations", domesticLocations);
        logger.info("Printing domestic locations: {}", domesticLocations);

        List<LocationDTO> internationalLocations = locationService.getOverseasLocations();
        model.addAttribute("internationalLocations", internationalLocations);
        logger.info("Printing international locations: {}", internationalLocations);

        String loginEmail = (String) session.getAttribute("email");
        System.out.println("세션 확인 = "+loginEmail);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("인증 확인 = "+authentication);


        String username = authentication.getName();
        model.addAttribute("email", username);
        System.out.println("유저 확인 = "+username);


        return "main"; // 메인 페이지로 이동
    }

    // 여행지 모달 불러오기
    @GetMapping("/location/{id}")
    public ResponseEntity<LocationDTO> getLocation(@PathVariable("id") Long locationId) {
        LocationDTO location = locationService.getLocationById(locationId);

        return ResponseEntity.ok(location);
    }
}