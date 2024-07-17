package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.EmailMessage;
import com.naver.OnATrip.entity.VerifyCode;
import com.naver.OnATrip.repository.VerifyRepository;
import com.naver.OnATrip.web.dto.member.VerifyCodeDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final VerifyRepository verifyRepository;
    private static int number;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(EmailMessage emailMessage) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage(); // MimeMessage 객체 생성
        try {
            // MimeMessageHelper를 사용하여 보다 쉽게 MimeMessage를 구성할 수 있다.
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // 이메일 수신자 설정
            mimeMessageHelper.setTo(emailMessage.getTo());

            // 이메일 제목 설정
            mimeMessageHelper.setSubject(emailMessage.getSubject());

            // 본문 내용 설정, false는 HTML 형식의 메세지를 사용하지 않음을 나타낸다.
            mimeMessageHelper.setText(emailMessage.getMessage(), false);

            // 이메일 발신자 설정
            mimeMessageHelper.setFrom(new InternetAddress(from + "@naver.com"));

            // 이메일 보내기
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw  new RuntimeException(e);
        }
    }

    public static void createNumber(){
        number = (int)(Math.random() * (90000)) + 100000;// (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public MimeMessage CreateMail(String mail){
        createNumber();
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(from+ "@naver.com");
            message.setRecipients(MimeMessage.RecipientType.TO, mail);
            message.setSubject("OnATrip 회원가입 인증 번호입니다.");
            String body = "";
            body += "<h1>" + number + "</h1>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return message;
    }

    public int sendNumber(String mail){

        MimeMessage message = CreateMail(mail);

        javaMailSender.send(message);

        return number;
    }

    public boolean  changePassword(VerifyCode verifyCode) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(from + "@naver.com");
            helper.setTo(verifyCode.getEmail());
            helper.setSubject("OnATrip 비밀번호 재설정 링크");
            String body = "<p>비밀번호 재설정을 위해 아래 링크를 클릭하세요:</p>"
                    + "<a href=\"http://localhost:9100/findPassword/" + verifyCode.getCode() + "\">"
                    + "비밀번호 재설정 링크</a>";

            helper.setText(body, true);

            javaMailSender.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
