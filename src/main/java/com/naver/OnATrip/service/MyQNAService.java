package com.naver.OnATrip.service;

import com.naver.OnATrip.constant.Role;
import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.MyQNA;
import com.naver.OnATrip.repository.MemberRepository;
import com.naver.OnATrip.repository.MyQNARepository;
import com.naver.OnATrip.web.dto.myQNA.CreateQNADto;
import com.naver.OnATrip.web.dto.myQNA.MyQNAListDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyQNAService {

    private final MyQNARepository myQNARepository;
    private final MemberRepository memberRepository;



    public void save(CreateQNADto createQNADto, String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElseThrow(() -> new IllegalArgumentException("해당 이메일로 등록된 사용자가 없습니다: " + email));

        createQNADto.setMember(member); // Member 객체 설정
        MyQNA myQNA = createQNADto.toEntity();
        myQNARepository.save(myQNA);
    }


}
