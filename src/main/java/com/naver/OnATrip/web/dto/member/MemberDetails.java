package com.naver.OnATrip.web.dto.member;

import com.naver.OnATrip.entity.Member;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;

@Data
public class MemberDetails implements UserDetails {

    private final Optional<Member> member;

    public MemberDetails(Optional<Member> member){
        this.member = member;
    }

    /* 유저 권한 목록 */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> collectors = new ArrayList<>();
//
//        collectors.add(() -> member.getRole());
//
//        return collectors;

        return null;
    }

    @Override
    public String getPassword() {
        return getPassword();
    }//----수정한 부분 --

    @Override
    public String getUsername() {
        return getUsername();
    }//----수정한 부분 --

    /* 계정 만료 여부, false : 만료 */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /* 계정 잠김 여부, false : 잠김 */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /* 비밀번호 만료 여부, false : 만료 */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /* 사용자 활성화 여부, false : 만료 */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
