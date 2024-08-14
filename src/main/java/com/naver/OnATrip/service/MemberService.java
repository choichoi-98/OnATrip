package com.naver.OnATrip.service;

import com.naver.OnATrip.constant.Role;
import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.repository.MemberRepository;
import com.naver.OnATrip.web.dto.member.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService  {
    //implements UserDetailsService

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public int join(MemberDTO memberDTO) {

        try {
            Member member = memberDTO.createMember();  // MemberDTO를 Member 객체로 변환
            String rawPass = member.getPassword();
            String encPass = passwordEncoder.encode(rawPass);
            member.setPassword(encPass);
            member.setRole(Role.valueOf("USER"));
            memberRepository.save(member);
            return 100;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("memberService " + e.getMessage());
        }
        return -100;
    }

    public boolean checkEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember==null){
            throw new UsernameNotFoundException(email);
        }

        Member member = optionalMember.get();

        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));

        return new org.springframework.security.core.userdetails.User(member.getEmail(), member.getPassword(), authorities);
    }

    public Member getUser(String email) {
        Optional<Member> member = this.memberRepository.findByEmail(email);
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new UsernameNotFoundException("siteuser not found");
        }
    }


    public MemberDTO login(MemberDTO memberDTO) {
        Optional<Member> memberEmail = memberRepository.findByEmail(memberDTO.getEmail());

        if (memberEmail.isPresent()) {
            Member member = memberEmail.get(); // Optional에서 꺼냄

            if (BCrypt.checkpw(memberDTO.getPassword(), member.getPassword())) {
                MemberDTO dto = MemberDTO.fromEntity(member);
                return dto;
            } else {
                System.out.println("비밀번호가 일치하지 않습니다.");
                return null;
            }
        } else {
            System.out.println("이메일로 사용자를 찾을 수 없습니다.");
            return null;
        }
    }

    public String updatePassword(String email, MemberDTO.PasswordDto passwordDto) {
        Member member = memberRepository.findByEmail(email).orElseGet(()-> null);
        passwordDto.setNewPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        member.updatePassword(passwordDto);
        return email;
    }

    public Member findByEmail(String email){
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()){
            return member.get();
        } else {
            throw new RuntimeException("Member not found : " + email);
        }
    }

    @Transactional
    public Member save(Member member) {
        return memberRepository.save(member);
    }
}


