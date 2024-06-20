package com.naver.OnATrip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PayController {

    @GetMapping("/subscribe")
    public String subscribe(){

        return "pay/subscribe" ;
    }

    @GetMapping("/payPage")
    public String pay(){

        return "pay/payPage" ;
    }


}
