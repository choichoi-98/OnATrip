package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.service.MyQNAService;
import com.naver.OnATrip.web.dto.myQNA.CreateQNADto;
import com.naver.OnATrip.web.dto.myQNA.MyQNAListDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class QNAController {

    private final MyQNAService myQNAService;

    @GetMapping("/myQNAList")
    public String QNAList() {
        return "myQNA/myQNAList";
        }

    @GetMapping("/createQNA")
    public String createQNA(Model model) {
        model.addAttribute("createQNADto", new CreateQNADto());
        return "myQNA/createQNA";
    }

    @PostMapping("/createQNA")
    public String save(@Valid CreateQNADto createQNADto, @RequestParam("file") List<MultipartFile> file,
                       Principal principal, Model model) {

        // 사용자 이름으로 Member 객체 생성
        Member writer = createMember(principal.getName()); //사용자 이름 반환
//        System.out.println("QNA Controller에 사용자 이름 = "+ writer);
           myQNAService.save(createQNADto);

        return "myQNA/createQNA";
    }

    public Member createMember(String username) {
        Member member = new Member();
        member.setName(username); // 사용자 이름으로 설정
        // 기타 필요한 필드 설정
        return member;
    }
    @GetMapping("/myQNAView")
    public String myQNAView(){
        return "myQNA/myQNAView";
    }

    @GetMapping("/memberQNA")
    public String memberQNA() {

        return "admin/memberQNA";
    }
}


