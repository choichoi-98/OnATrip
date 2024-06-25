package com.naver.OnATrip.controller;

import com.naver.OnATrip.service.MemberService;
import com.naver.OnATrip.web.dto.member.MemberDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String login(){

        return "member/login"
                ;
    }

    @GetMapping("/findPassword")
    public String findPassword(){

        return "member/findPassword"
                ;
    }

    @GetMapping("/join")
    public String join(){

        return "member/join"
                ;
    }

    @PostMapping("/join")
    public String joinProcess(@Valid MemberDTO memberDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("MemberDTO", memberDTO);
            return "member/join";
        }
        if (!memberDTO.getPassword().equals(memberDTO.getPasswdCheck())) {
            bindingResult.rejectValue("accountPasswdCheck", "passwdIncorrect", "비밀번호가 일치하지 않습니다");
            return "member/join";
        }

        try {
            int result = memberService.join(memberDTO);
        } catch (
                DataIntegrityViolationException e){
            e.printStackTrace();
            bindingResult.reject("signUpFailed","이미 등록된 사용자입니다.");
            return "Member/join";
        } catch (Exception e){
            e.printStackTrace();
            bindingResult.reject("signUpFailed",e.getMessage());
            return "Member/join";
        }

        System.out.println("@@@@@ MemberController");

        return "redirect:login";
    }



}
