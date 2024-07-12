package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Location;
import com.naver.OnATrip.service.LocationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    private final LocationService locationService;

    public MainController(LocationService locationService) {
        this.locationService = locationService;
    }

    // 국내 여행지
    @GetMapping("/domestic")
    public String getDomesticLocations(Model model) {
        List<Location> domesticLocations = locationService.getDomesticLocations();
        model.addAttribute("domesticLocations", domesticLocations);
        return "main"; // 이 부분은 실제로는 domestic 탭에 대응하는 뷰를 리턴해야 합니다.
    }

    // 해외 여행지
    @GetMapping("/overseas")
    public String getOverseasLocations(Model model) {
        List<Location> overseasLocations = locationService.getOverseasLocations();
        model.addAttribute("overseasLocations", overseasLocations);
        return "main"; // 이 부분은 실제로는 overseas 탭에 대응하는 뷰를 리턴해야 합니다.
    }

}
