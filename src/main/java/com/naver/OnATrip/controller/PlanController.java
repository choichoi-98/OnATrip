package com.naver.OnATrip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class PlanController {

    @GetMapping("/planCreate")
    public String planCreate(){
        return "plan/createPlan";
    }

}
