package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.repository.MemberRepository;
import com.naver.OnATrip.web.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public int join(MemberDTO memberDTO) {

        try {
            Member member = memberDTO.toEntity();  // MemberDTO를 Member 객체로 변환
            String rawPass = member.getPassword();
            String encPass = passwordEncoder.encode(rawPass);
            member.setPassword(encPass);
            member.setRole("USER");
            memberRepository.join(member);
            return 100;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("memberService " + e.getMessage());
        }
        return -100;
    }

}


