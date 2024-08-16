package com.naver.OnATrip.web.dto.member;


import com.naver.OnATrip.constant.Role;
import com.naver.OnATrip.entity.Member;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class MemberDTO {
    private Long id;
//    @NotNull - Null 허용하지 않는 것 - String 외 타입에 사용
//    @NotEmpty - Null 과 "" 둘 다 허용하지 않는 것 - String, List<> 에만 사용!
//    @NotBlank - Null 과 "", " " 모두 허용하지 않는 것 - String에만 사용!
    @NotBlank(message = "메일주소를 입력해주세요")
    @Email
    private String email;

    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message= "비밀번호는 숫자,영문,특수문자 포함 8~16자리 입니다.")
    @Size(min=8, max=16, message= "")
    private String password;

    @NotBlank(message = "비밀번호 한번 더 입력해주세요")
    private String passwdCheck;

    @NotBlank(message = "이름을 입력해주세요")
    @Size(min = 2, max=10, message = "이름은 한글자 이상이어야 합니다.")
    private String name;

    private Role role;

    private String subscribe_status = "OFF";

    public MemberDTO(String email, String password, String passwdCheck,String name, Role role, String subscribe_status) {
        this.email = email;
        this.password = password;
        this.passwdCheck = passwdCheck;
        this.name = name;
        this.role = role;
        this.subscribe_status = subscribe_status;
    }

    public MemberDTO() {
    }

    public MemberDTO(String email, String password) {
    }

    public Member createMember() {
        Member member = Member.builder()
                .name(name)
                .password(password)
                .email(email)
                .role(role)
                .subscribe_status(subscribe_status)
                .build();
        return member;
    }

    // Member 엔티티를 MemberDTO로 변환하는 메서드
    public static MemberDTO fromEntity(Member member) {
        MemberDTO dto = new MemberDTO();
        dto.setId(member.getId());
        dto.setEmail(member.getEmail());
        dto.setName(member.getName());
        dto.setRole(member.getRole()); // Member 엔티티에 role 필드가 있다면 가져와서 설정

        return dto;
    }

    // 비밀번호 변경을 위한 내부 클래스
    @Getter
    @Setter
    public static class PasswordDto {
        @NotBlank(message = "새 비밀번호를 입력해주세요")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message= "비밀번호는 숫자,영문,특수문자 포함 8~16자리 입니다.")
        private String newPassword;

        @NotBlank(message = "새 비밀번호를 한번 더 입력해주세요")
        private String newPasswordCheck;
    }
}
