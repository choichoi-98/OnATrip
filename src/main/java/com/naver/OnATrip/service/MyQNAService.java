package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.MyQNA;
import com.naver.OnATrip.repository.MemberRepository;
import com.naver.OnATrip.repository.myQNA.MyQNARepository;
import com.naver.OnATrip.web.dto.myQNA.CreateQNADto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyQNAService {

    private final MyQNARepository myQNARepository;
    private final MemberRepository memberRepository;



    public void save(CreateQNADto createQNADto, String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElseThrow(() -> new IllegalArgumentException("해당 이메일로 등록된 사용자가 없습니다: " + email));

        // CreateQNADto를 MyQNA로 변환
        MyQNA myQNA = createQNADto.toEntity();
        myQNA.setMember(member); // Member 객체 설정

        if (myQNA.getId() != null && myQNARepository.existsById(myQNA.getId())) {
            // 기존 데이터가 있으면 업데이트
            MyQNA existingQNA = myQNARepository.findById(myQNA.getId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 QNA가 존재하지 않습니다: " + myQNA.getId()));

            existingQNA.setTitle(myQNA.getTitle());
            existingQNA.setContent(myQNA.getContent());
            existingQNA.setQnaStatus(myQNA.getQnaStatus());
            existingQNA.setMember(member);
            existingQNA.setAnswer(myQNA.getAnswer());

            myQNARepository.save(existingQNA);
        } else {
            // 새 데이터가 있으면 삽입
            myQNARepository.save(myQNA);
        }
    }

    public List<MyQNA> findMyQNA(String email) {
        return myQNARepository.findAll(email);
    }

    @Transactional(readOnly = true)
    public Optional<MyQNA> getQNADetail(Long id) {

        return myQNARepository.findById(id);
        }

    @Transactional
    public void delete(Long id) {
        myQNARepository.deleteById(id);
    }

}
