package com.naver.OnATrip.entity;

import com.naver.OnATrip.constant.Role;
import com.naver.OnATrip.entity.pay.Subscribe;
import com.naver.OnATrip.web.dto.member.MemberDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter @Setter
@Builder

public class Member implements UserDetails {

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

    private String subscribe_status = "OFF";

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Subscribe> subscribes = new ArrayList<>();

    public Member(Long id, String email, String password, String name, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    public Member(Long id, String email, String password, String name, Role role, String subscribe_status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.subscribe_status = subscribe_status;
    }

    public Member(Long id, String email, String password, String name, Role role, String subscribe_status, List<Subscribe> subscribes) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.subscribe_status = subscribe_status;
        this.subscribes = subscribes;
    }

    public Member() {

    }

    public void updatePassword(MemberDTO.PasswordDto passwordDto) {
        this.password = passwordDto.getNewPassword();
    }

    //로그인시 비밀번호 확인
    public boolean passwdCheck(String pass){
        return this.password.equals(pass);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority(role.getValue()));
        return list;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
