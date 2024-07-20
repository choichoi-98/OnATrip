package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.MyQNA;
import com.naver.OnATrip.repository.MemberRepository;
import com.naver.OnATrip.service.MyQNAService;
import com.naver.OnATrip.web.dto.myQNA.CreateQNADto;
import com.naver.OnATrip.web.dto.myQNA.MyQNAListDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class QNAController {

    private final MyQNAService myQNAService;
    private final MemberRepository memberRepository;

    //질문게시판리스트
    @GetMapping("/myQNAList")
    public String QNAList(Model model, Principal principal){

        String email = principal.getName();

        List<MyQNA> myQNA = myQNAService.findMyQNA(email);

        model.addAttribute("myQNA",myQNA);
        return "myQNA/myQNAList";
        }

    // 질문게시판 작성
    @GetMapping("/createQNA")
    public String createQNA(Model model) {
        model.addAttribute("createQNADto", new CreateQNADto());
        return "myQNA/createQNA";
    }

    @PostMapping("/createQNA")
    public String save(@Valid CreateQNADto createQNADto, @RequestParam("file") List<MultipartFile> file,
                       Principal principal, Model model) {

        String email = principal.getName(); // 현재 로그인한 사용자의 이메일
        System.out.println("컨트롤러에서 이메일 확인 =" + email) ;
        myQNAService.save(createQNADto, email);

        return "redirect:/myQNAList";
    }

    //질문게시판 상세 페이지
    @GetMapping("/myQNAView/{myQNAId}")
    public String myQNAView(Model model, @PathVariable("myQNAId") Long myQNAId){
        System.out.println("컨트롤러에서 받아오는 아이디=" + myQNAId);

        Optional<MyQNA> myQNAViewDto = myQNAService.getQNADetail(myQNAId);

        if (myQNAViewDto.isPresent()) {
            model.addAttribute("myQNA", myQNAViewDto.get());
        }
            return "myQNA/myQNAView";
    }

    //질문 수정
    @GetMapping("/update/{myQNAId}")
    public String updateQNA(@PathVariable("myQNAId") Long myQNAId, Model model){
        Optional<MyQNA> createQNADto = myQNAService.getQNADetail(myQNAId);

        model.addAttribute("createQNADto", createQNADto);
        return "myQNA/updateQNA";
    }

    @PostMapping("/update/{myQNAId}")
    public String updateQNAProc(@PathVariable("myQNAId") Long myQNId, Model model){
        return null;
    }
    //질문 삭제
    @Transactional
    @PostMapping("/delete/{myQNAId}")
    public String delete(@PathVariable("myQNAId") Long myQNAId){
         myQNAService.delete(myQNAId);
        return "redirect:/myQNAList";
    }

    //관리자가 보는 질문게시판
    @GetMapping("/memberQNA")
    public String memberQNA() {

        return "admin/memberQNA";
    }

    public Member createMember(String username) {
        Member member = new Member();
        member.setName(username); // 사용자 이름으로 설정
        // 기타 필요한 필드 설정
        return member;
    }
}


