package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Location;
import com.naver.OnATrip.service.LocationService;
import com.naver.OnATrip.web.dto.location.LocationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    private final LocationService locationService;

    public MainController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/")
    public String main(Model model) {
        List<LocationDTO> allLocations = locationService.getAllLocations();
        model.addAttribute("allLocations", allLocations);
        logger.debug("Printing all locations: {}", allLocations);

        List<LocationDTO> domesticLocations = locationService.getDomesticLocations();
        model.addAttribute("domesticLocations", domesticLocations);
        logger.debug("Printing domestic locations: {}", domesticLocations);

        List<LocationDTO> internationalLocations = locationService.getOverseasLocations();
        model.addAttribute("internationalLocations", internationalLocations);
        logger.debug("Printing international locations: {}", internationalLocations);

        logger.debug("All locations count: {}", allLocations.size());
        logger.debug("Domestic locations count: {}", domesticLocations.size());
        logger.debug("International locations count: {}", internationalLocations.size());

        return "main";
    }

}