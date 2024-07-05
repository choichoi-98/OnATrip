package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.service.MemberService;
import com.naver.OnATrip.web.dto.member.MemberDTO;
import com.naver.OnATrip.web.dto.member.MemberDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @GetMapping("/login")
    public String login() {

        return "member/login"
                ;
    }

//    @PostMapping("/login")
//    public String login(String email, String password, HttpSession session,Model model) {
//        UserDetails member = memberService.loadUserByUsername(email);
//        if (member != null && MemberDTO.getPassword().equals(member.get)) {
//            session.setAttribute("member", member);
//            return "redirect:/main";
//        } else {
//            model.addAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
//            return "member/login";
//        }
//    }

    @PostMapping("/login")
    public String loginProc(@ModelAttribute MemberDTO memberDTO, HttpSession session, Model model) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // login 성공
            System.out.println("로그인 성공");
            session.setAttribute("loginEmail", loginResult.getEmail());
            return "main";
        } else {
            // login 실패
            System.out.println("로그인 실패: 이메일이나 비밀번호가 잘못되었습니다.");
            model.addAttribute("이메일이나 비밀번호가 잘못되었습니다.");
            return "member/login";
        }
    }

    @GetMapping("/findPassword")
    public String findPassword() {

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

    //회원가입시 이메일 중복 확인
    @PostMapping("checkEmail")
    @ResponseBody
    public boolean checkEmail(@RequestParam("email") String email) {

        return memberService.checkEmail(email);
    }
}

