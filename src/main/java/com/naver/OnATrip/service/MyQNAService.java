package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.MyQNA;
import com.naver.OnATrip.repository.MyQNARepository;
import com.naver.OnATrip.web.dto.myQNA.CreateQNADto;
import com.naver.OnATrip.web.dto.myQNA.MyQNAListDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class MyQNAService {

    private final MyQNARepository myQNARepository;

    public void save(CreateQNADto createQNADto) {
        MyQNA myQNA = createQNADto.toEntity();
        myQNARepository.save(myQNA);
    }


}
