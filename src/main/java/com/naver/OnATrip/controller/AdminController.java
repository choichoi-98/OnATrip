package com.naver.OnATrip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String admin(Model model) {
        return "admin/adminMain";
    }


    @GetMapping("/test")
    public String test(Model model) {
        return "header_layout";
    }


}
