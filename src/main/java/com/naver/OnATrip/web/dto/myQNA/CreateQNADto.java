package com.naver.OnATrip.web.dto.myQNA;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.MyQNA;
import com.naver.OnATrip.service.MyQNAService;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class CreateQNADto {

    private Long id;

    private String qnaStatus;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private String writer;

    private LocalDate createdAt;

    private LocalDate modifyDate;

    private String answer="N"; // 답변 달림 여부 Y, N

    private Member member;

    private MultipartFile file;

    private String image;



    public CreateQNADto() {
        this.createdAt = LocalDate.now(); // 기본 생성자에서 현재 날짜로 초기화
    }


    public MyQNA toEntity() {
        return MyQNA.builder()
                .qnaStatus(qnaStatus)
                .title(title)
                .content(content)
                .writer(member.getName())
                .createdAt(createdAt)
                .member(member) // Member 객체 설정
                .answer(answer)
                .build();
    }

    @Builder
    public CreateQNADto(Member member){
        MyQNA myQNA = new MyQNA();
        this.qnaStatus = myQNA.getQnaStatus();
        this.title = myQNA.getTitle();
        this.content = myQNA.getContent();
        this.member = myQNA.getMember(); // MyQNA 객체의 member 필드를 가져옴
        this.writer = myQNA.getMember().getName(); // 작성자 이름 설정
        this.answer = myQNA.getAnswer();
    }
}