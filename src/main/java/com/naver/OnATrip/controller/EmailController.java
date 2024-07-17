package com.naver.OnATrip.controller;


import com.naver.OnATrip.entity.EmailMessage;
import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.VerifyCode;
import com.naver.OnATrip.repository.MemberRepository;
import com.naver.OnATrip.service.EmailService;
import com.naver.OnATrip.service.VerifyCodeService;
import com.naver.OnATrip.web.dto.member.MemberDTO;
import com.naver.OnATrip.web.dto.member.VerifyCodeDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class EmailController {

    private final VerifyCodeService verifyCodeService;
    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);


//    @PostMapping("/sendEmail")
//    public String sendEmail(@RequestParam("email") String email, MemberDTO memberDTO, Model model) {
//
//        Boolean emailExists = memberRepository.existsByEmail(email);
//
//        if (!emailExists) {
//            // 이메일이 존재하지 않는 경우
//            logger.info("이메일이 존재하지 않음: " + email);
//            model.addAttribute("result", "등록된 이메일이 아닙니다.");
//            return "member/findPassword";
//        }
//
//         EmailMessage emailMessage = EmailMessage.builder()
//                .to(email)
//                .subject("비밀번호를 변경해주세요")
//                .message("비밀번호 재설정을 위한 이메일입니다.")
//                .build();
//        emailService.sendMail(emailMessage);
//
//        System.out.println("메일보내기 성공");
//        model.addAttribute("success", "메일 보내기 성공. 이메일을 확인해주세요.");
//        return "member/login";
//    }

    @PostMapping(value = "/sendEmail")
    public ResponseEntity<HashMap> mailCheck(VerifyCodeDto codeDto){

        VerifyCode verifyCode = verifyCodeService.saveCode(codeDto);

        boolean success = emailService.changePassword(verifyCode);

        HashMap<String, Object> responseMap = new HashMap<>();
        if (success) {
            responseMap.put("status", 200);
            responseMap.put("message", "메일 발송 성공");
            responseMap.put("code", verifyCode.getCode());
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.OK);
        } else {
            responseMap.put("status", 500);
            responseMap.put("message", "메일 발송 실패");
            return new ResponseEntity<HashMap>(responseMap, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ResponseBody
    @PostMapping("/mail")
    public String MailSend(@RequestParam("email") String email){

        int number = emailService.sendNumber(email);

        String num = "" + number;

        return num;
    }
}

