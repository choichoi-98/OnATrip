package com.naver.OnATrip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class WebController {

    @GetMapping("/main")
    public String main(Model model) {
        return "main";
    }

    @GetMapping("/test")
    public String test(Model model) {
        return "header_layout.html";
    }
}
