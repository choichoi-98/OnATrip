package com.naver.OnATrip.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    @GetMapping("/login")
    public String login(){

        return "member/login"
                ;
    }

    @GetMapping("/find-password")
    public String findPassword(){

        return "member/find-password"
                ;
    }

    @GetMapping("/join")
    public String join(){

        return "member/join"
                ;
    }
}
