package com.naver.OnATrip.controller;

import com.naver.OnATrip.service.MemberService;
import com.naver.OnATrip.web.dto.member.MemberDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @GetMapping("/login")
    public String login(){

        return "member/login"
                ;
    }

    @PostMapping("/login")
    public String loginProc(){

        System.out.println("@@@@@ MemberLogin Success");

        return "main";

    }

    @GetMapping("/findPassword")
    public String findPassword(){

        return "member/findPassword"
                ;
    }

    @GetMapping("/join")
    public String join(Model model) {
        logger.info("MemberController, join");
        model.addAttribute("memberDTO", new MemberDTO());

        return "member/join";
    }
    @PostMapping("/join")
    public String joinProcess(@Valid MemberDTO memberDTO, BindingResult bindingResult, Model model) {

            if (bindingResult.hasErrors()) {
                model.addAttribute("MemberDTO", memberDTO);
                return "member/join";
            }

//            if (memberService.checkEmail(memberDTO.getEmail())) {
//                bindingResult.addError(new FieldError("memberDTO", "email", "로그인 아이디가 중복됩니다."));
//            }

            if (!memberDTO.getPassword().equals(memberDTO.getPasswdCheck())) {
                bindingResult.rejectValue("PasswdCheck", "passwdIncorrect", "비밀번호가 일치하지 않습니다");
                return "member/join";
            }

            try {
                int result = memberService.join(memberDTO);
            } catch (
                    DataIntegrityViolationException e) {
                e.printStackTrace();
                bindingResult.reject("signUpFailed", "이미 등록된 사용자입니다.");
                return "Member/join";
            } catch (Exception e) {
                e.printStackTrace();
                bindingResult.reject("signUpFailed", e.getMessage());
                return "Member/join";
            }

            System.out.println("@@@@@ MemberController");

            return "redirect:login";
        }

}
