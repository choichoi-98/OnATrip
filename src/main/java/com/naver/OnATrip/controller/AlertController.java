package com.naver.OnATrip.controller;

import com.naver.OnATrip.service.AlertService;
import com.naver.OnATrip.web.dto.AlertDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;


}
