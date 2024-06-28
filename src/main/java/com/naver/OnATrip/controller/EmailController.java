package com.naver.OnATrip.controller;


import com.naver.OnATrip.entity.EmailMessage;
import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.repository.MemberRepository;
import com.naver.OnATrip.service.EmailService;
import com.naver.OnATrip.web.dto.member.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @PostMapping("/sendEmail")
    public String sendEmail(MemberDTO memberDTO, Model model) {

        Boolean emailExists = memberRepository.existsByEmail(memberDTO.getEmail());

        if (!emailExists) {
            // 이메일이 존재하지 않는 경우
            logger.info("이메일이 존재하지 않음: " + memberDTO.getEmail());
            return "member/findPassword";
        }

         EmailMessage emailMessage = EmailMessage.builder()
                .to(memberDTO.getEmail())
                .subject("비밀번호를 변경해주세요")
                .message("비밀번호 재설정을 위한 이메일입니다.")
                .build();
        emailService.sendMail(emailMessage);

        logger.info("메일보내기 성공 = "+ emailMessage);

        System.out.println("메일보내기 성공");
        model.addAttribute("success", "메일 보내기 성공. 이메일을 확인해주세요.");
        return "member/login";
    }
}

