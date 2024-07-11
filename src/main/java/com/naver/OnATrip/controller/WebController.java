package com.naver.OnATrip.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class WebController {

    @GetMapping("/main")
    public String main( HttpSession session, Model model) {


        String loginEmail = (String) session.getAttribute("email");
        System.out.println("세션 확인 = "+loginEmail);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("인증 확인 = "+authentication);

        // 로그인된 이메일이 없으면 로그인 페이지로 이동
        if (loginEmail == null) {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }
        // 로그인된 이메일을 모델에 추가하여 메인 페이지에 표시
        model.addAttribute("email", loginEmail);

        return "main"; // 메인 페이지로 이동
    }


}
