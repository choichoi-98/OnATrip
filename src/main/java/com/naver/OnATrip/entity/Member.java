package com.naver.OnATrip.entity;

import com.naver.OnATrip.web.dto.member.MemberDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Builder

public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    private String name;

    private String role;

    public Member(Long id, String email, String password, String name, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Member() {

    }

    //로그인시 비밀번호 확인
    public boolean passwdCheck(String pass){
        return this.password.equals(pass);
    }

    public static Member toMember(MemberDTO memberDTO){
        Member member = new Member();
        member.setEmail(memberDTO.getEmail());
        member.setName(memberDTO.getName());
        member.setPassword(memberDTO.getPassword());
        return member;
    }
}
