package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.MyQNA;
import com.naver.OnATrip.repository.MemberRepository;
import com.naver.OnATrip.repository.myQNA.MyQNARepository;
import com.naver.OnATrip.web.dto.myQNA.CreateQNADto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MyQNAService {

    private final MyQNARepository myQNARepository;
    private final MemberRepository memberRepository;



    public void save(CreateQNADto createQNADto, String email) throws IOException {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElseThrow(() -> new IllegalArgumentException("해당 이메일로 등록된 사용자가 없습니다: " + email));

        // CreateQNADto를 MyQNA로 변환
        MyQNA myQNA = createQNADto.toEntity();
        myQNA.setMember(member); // Member 객체 설정
        myQNA.setWriter(member.getName()); // Writer를 Member의 이름으로 설정

        if (myQNA.getId() != null && myQNARepository.existsById(myQNA.getId())) {
            // 기존 데이터가 있으면 업데이트
            MyQNA existingQNA = myQNARepository.findById(myQNA.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 QNA가 존재하지 않습니다: " + myQNA.getId()));

            existingQNA.setTitle(myQNA.getTitle());
            existingQNA.setContent(myQNA.getContent());
            existingQNA.setQnaStatus(myQNA.getQnaStatus());
            existingQNA.setMember(member);
            existingQNA.setWriter(myQNA.getWriter()); // 기존 writer 값을 업데이트
            existingQNA.setAnswer(myQNA.getAnswer());

            myQNARepository.save(existingQNA);
        } else {
            // 새 데이터가 있으면 삽입
            myQNARepository.save(myQNA);
        }
    }


    public Page<MyQNA> getList(int page, String email) {
        Pageable pageable = PageRequest.of(page, 10); // 페이지 크기는 필요에 따라 조정
        return myQNARepository.findByMemberEmail(email, pageable);
    }


    @Transactional(readOnly = true)
    public Optional<MyQNA> getQNADetail(Long id) {

        return myQNARepository.findById(id);
        }

    @Transactional
    public void delete(Long id) {
        myQNARepository.deleteById(id);
    }

    public List<MyQNA> findAll() {
        return myQNARepository.findAll();
    }

    public Page<MyQNA> getList(int page) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by(Sort.Order.desc("createdAt")));
        return this.myQNARepository.findAll(pageable);
    }

    public boolean saveReply(Long id, String reply) {
        MyQNA qna = myQNARepository.findById(id).orElse(null);
        if (qna != null) {
            qna.setReply(reply);
            qna.setAnswer("Y");
            myQNARepository.save(qna);
            return true;
        }
        return false;
    }

    public boolean clearReply(Long id) {
        try {
            // 댓글을 빈 문자열로 업데이트하는 로직
            MyQNA myQNA = myQNARepository.findById(id).orElseThrow(() -> new RuntimeException("문의가 없습니다."));
            myQNA.setReply(null);
            myQNA.setAnswer("N");
            myQNARepository.save(myQNA);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 실패 시 false 반환
        }
    }
}
