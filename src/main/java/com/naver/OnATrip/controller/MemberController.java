package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.VerifyCode;
import com.naver.OnATrip.service.EmailService;
import com.naver.OnATrip.service.MemberService;
import com.naver.OnATrip.service.VerifyCodeService;
import com.naver.OnATrip.web.dto.member.MemberDTO;
import com.naver.OnATrip.web.dto.member.MemberDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final VerifyCodeService verifyCodeService;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @GetMapping("/login")
    public String login() {

        return "member/login"
                ;
    }

    @PostMapping("/loginProc")
    public String loginProc(@ModelAttribute MemberDTO memberDTO, HttpSession session, Model model) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // login 성공
            System.out.println("로그인 성공" + loginResult.getEmail());
            session.setAttribute("email", loginResult.getEmail());
            return "redirect:/main";
        } else {
            // login 실패
            System.out.println("로그인 실패: 이메일이나 비밀번호가 잘못되었습니다.");
            model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요");
            return "member/login";
        }
    }

    @GetMapping("/findPassword")
    public String findPassword() {

        return "member/findPassword"
                ;
    }

    @GetMapping(value = "/findPassword/{code}")
    public String changePasswordForm(@PathVariable("code") String code, Model model) {
        model.addAttribute("code", code);
        System.out.println("받아온 code는 무엇인가 = "+ code);
        return "member/changePassword";
    }
    @PostMapping(value = "/findPassword/{code}")
    public String changePassword(
            @RequestParam("newPassword") String newPassword,
            @RequestParam("newPasswordCheck") String newPasswordCheck,
            @PathVariable("code") String code) {

        VerifyCode verifyCode = verifyCodeService.findByCodeConfirm(code);
        System.out.println("멤버컨트롤러 verifyCode = " + verifyCode);

        if (verifyCode == null) {
            System.out.println("만료되었거나 잘못된 링크");
            return "redirect:/findPassword/" + code;
        }

        // 패스워드 확인이 일치하는지 검증
        if (!newPassword.equals(newPasswordCheck)) {
            System.out.println("비밀번호 확인이 일치하지 않습니다.");
            return "redirect:/findPassword/" + code;
        }

        // 패스워드 변경 로직
        MemberDTO.PasswordDto passwordDto = new MemberDTO.PasswordDto();
        passwordDto.setNewPassword(newPassword);

        memberService.updatePassword(verifyCode.getEmail(), passwordDto);

        if (true) {
            verifyCodeService.deleteByEmail(verifyCode.getEmail());
            verifyCodeService.deleteCode(code);
            System.out.println("비밀번호 변경 성공");
            return "member/login";
        } else {
            System.out.println("비밀번호 변경 실패");
            return "redirect:/findPassword/" + code;
        }
    }

    @GetMapping("/join")
    public String join(Model model) {
        logger.info("--------------------------------------------MemberController, join");
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

