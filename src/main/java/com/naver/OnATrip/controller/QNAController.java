package com.naver.OnATrip.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class QNAController {

    @GetMapping("/myQNAList")
    public String QNAList() {

        return "myQNA/myQNAList";
    }

    @GetMapping("/myQNADetail")
    public String QNADetail() {

        return "myQNA/myQNADetail";
    }

    @GetMapping("/memberQNA")
    public String memberQNA() {

        return "admin/memberQNA";
    }
}


