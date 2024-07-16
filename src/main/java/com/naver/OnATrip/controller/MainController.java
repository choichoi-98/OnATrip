package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Location;
import com.naver.OnATrip.service.LocationService;
import com.naver.OnATrip.web.dto.location.LocationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final LocationService locationService;

    public MainController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/")
    // 메인 주의 여기 주의 주의 주의 주의
    public String main(Model model) {
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


        return "main";
    }

    // 여행지 모달 불러오기
    @GetMapping("/location/{id}")
    public ResponseEntity<LocationDTO> getLocation(@PathVariable("id") Long locationId) {
        LocationDTO location = locationService.getLocationById(locationId);

        return ResponseEntity.ok(location);
    }
}