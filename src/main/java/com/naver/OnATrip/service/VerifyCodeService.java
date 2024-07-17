package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.VerifyCode;
import com.naver.OnATrip.repository.VerifyRepository;
import com.naver.OnATrip.web.dto.member.VerifyCodeDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VerifyCodeService {

    private final VerifyRepository verifyRepository;

    public VerifyCode saveCode(VerifyCodeDto codeDto) {
        // 이미 해당 이메일이 존재한다면 삭제
        if (verifyRepository.existsByEmail(codeDto.getEmail())) {
            verifyRepository.deleteByEmail(codeDto.getEmail());
        }
        // 새로운 VerifyCode 엔티티 생성 및 저장
        VerifyCode verifyCode = new VerifyCode(codeDto);

        return verifyRepository.save(verifyCode);
    }


    public VerifyCode findByCode(String code) {

        VerifyCode verifyCode = verifyRepository.findByCode(code);
        if(verifyCode != null) verifyRepository.deleteByEmail(verifyCode.getEmail());
        return verifyCode;
    }

    public void deleteByEmail(String email) {
        verifyRepository.deleteByEmail(email);
    }

    public VerifyCode findByCodeConfirm(String code) {
        VerifyCode verifyCode = verifyRepository.findByCode(code);
        return verifyCode;
    }

    public void deleteCode(String code){
         verifyRepository.deleteByCode(code);
    }
}
