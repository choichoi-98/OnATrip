package com.naver.OnATrip.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="QNA")
public class MyQNA {

    @Id
    @Column(name="QNA_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String qnaStatus;

    private String title;

    private String content;

    private String writer;

    @CreatedDate
    private LocalDate createAt;

    @LastModifiedDate
    private LocalDate modifyDate;

    @Column
    private String is_deleted; //삭제 여부

    @Column
    private String answer = "N"; // 답변 달림 여부 Y, N // 기본값 'N' 설정

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    private String file;

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void is_deleted(String is_deleted){
        this.is_deleted = is_deleted;
    }

    @Builder
    public MyQNA(String qnaStatus, String title, String content, String writer, Member member, String answer){
        this.qnaStatus = qnaStatus;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.answer = "N";
    }


}
