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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);


    @PostMapping("/sendEmail")
    public String sendEmail(Model model, @RequestParam("email") String email){
        logger.info(String.format("findPassword.html에서 넘어온 이메일 : ", email));

        try{
            Boolean result = memberRepository.existsByEmail(email);

            if(result){
                model.addAttribute("result", "메일을 전송하였습니다.");
            } else {
                model.addAttribute("result","메일 전송 실패! 관리자에게 문의하세요");
            }
        } catch (Exception e){
            model.addAttribute("result", "등록된 이메일이 없습니다.");
        }

        return "member/findPassword";
    }
//
//    @PostMapping("/sendEmail")
//    public String sendEmail(@RequestParam("email") String email, MemberDTO memberDTO, Model model) {
//
//        Boolean emailExists = memberRepository.existsByEmail(email);
//
//        if (!emailExists) {
//            // 이메일이 존재하지 않는 경우
//            logger.info("이메일이 존재하지 않음: " + email);
//            model.addAttribute("error", "등록된 이메일이 아닙니다.");
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

    @ResponseBody
    @PostMapping("/mail")
    public String MailSend(@RequestParam("email") String email){

        int number = emailService.sendNumber(email);

        String num = "" + number;

        return num;
    }
}

