package com.naver.OnATrip.web.dto.member;


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
    @Size(min=8, max=20)
    private String password;

    @NotBlank(message = "비밀번호 한번 더 입력해주세요")
    private String passwdCheck;

    @NotBlank(message = "이름을 입력해주세요")
    @Size(min = 2, max=10)
    private String name;

    private String role;

    public MemberDTO(String email, String password, String passwdCheck,String name) {
        this.email = email;
        this.password = password;
        this.passwdCheck = passwdCheck;
        this.name = name;
    }

    public MemberDTO() {
    }

    public Member toEntity() {
        Member member = Member.builder()
                .name(name)
                .password(password)
                .email(email)
                .build();
        return member;
    }
}
