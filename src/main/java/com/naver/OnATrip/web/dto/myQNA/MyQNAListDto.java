package com.naver.OnATrip.web.dto.myQNA;

import com.naver.OnATrip.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class MyQNAListDto {

    private Long id;

    private String qnaStatus;

    private String title;

    private String content;

    private String writer;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime modifyDate;

    private String answer;//답변 달림 여부 Y, N

    private Member member;

    private boolean file;

    //검색
    @Getter
    @Setter
    public class SearchDto {
        private String Keyword; //검색키워드
        private String searchVal; //검색값
    }
}
