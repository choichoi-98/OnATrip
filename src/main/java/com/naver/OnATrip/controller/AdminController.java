package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Location;
import com.naver.OnATrip.repository.admin.LocationRepository;
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

    @Autowired
    private LocationRepository locationRepository;

    @GetMapping("/admin")
    public String admin(Model model) {
        return "admin/adminMain";
    }

    @PostMapping("/admin/addLocation")
    public String addLocation(@RequestParam("country") String country,
                              @RequestParam("city") String city,
                              @RequestParam("description") String description,
                              @RequestParam("file") MultipartFile file,
                              @RequestParam("locationType") String locationType) throws IOException {

        Location location = new Location();
        location.setCountryName(country);
        location.setCity(city);
        location.setDescription(description);
        location.setImage(file.getBytes());

        //여행지 추가
        locationRepository.addLocation(location);

        return "success";
    }


}
