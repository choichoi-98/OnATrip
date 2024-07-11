package com.naver.OnATrip.entity;

import com.naver.OnATrip.constant.Role;
import com.naver.OnATrip.web.dto.member.MemberDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Entity
@Getter @Setter
@Builder

public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member(Long id, String email, String password, String name, Role role) {
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


}
