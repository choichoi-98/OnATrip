package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.MyQNA;
import com.naver.OnATrip.repository.MemberRepository;
import com.naver.OnATrip.service.MemberService;
import com.naver.OnATrip.service.MyQNAService;
import com.naver.OnATrip.web.dto.myQNA.CreateQNADto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class QNAController {

    private final MyQNAService myQNAService;
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    //질문게시판리스트
    @GetMapping("/myQNAList")
    public String QNAList(Model model, @RequestParam(value="page", defaultValue="0") int page, Principal principal) {

        String email = principal.getName();

        // 이메일을 전달하여 필터링된 페이지 결과를 가져옵니다.
        Page<MyQNA> paging = this.myQNAService.getList(page, email);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        // 날짜 포맷팅
        List<MyQNA> formattedQNAList = paging.getContent().stream()
                .map(qna -> {
                    qna.setFormattedCreatedAt(qna.getCreatedAt().format(formatter));
                    return qna;
                })
                .collect(Collectors.toList());

        // 모델에 속성 설정
        model.addAttribute("paging", paging);
        model.addAttribute("myQNA", formattedQNAList);

        return "myQNA/myQNAList";
    }


    // 질문게시판 작성
    @GetMapping("/createQNA")
    public String createQNA(Model model) {
        model.addAttribute("createQNADto", new CreateQNADto());
        return "myQNA/createQNA";
    }

    @PostMapping("/createQNA")
    public String save(@Valid CreateQNADto createQNADto, @RequestParam("file") MultipartFile file,
                       Principal principal, Model model, BindingResult result) throws IOException {

        if (result.hasErrors()) {
            return "redirect:/createQNA";
        }

        String uploadDir =  "src/main/resources/static/images/myQNA";
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        if (!file.isEmpty()) {
            try {
                String filePath = uploadDir + file.getOriginalFilename();
                File destinationFile = new File(filePath);
                file.transferTo(destinationFile);
                System.out.println("파일 저장 경로: " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
                // 파일 저장 실패 시 예외 처리
            }
        }


        String email = principal.getName(); // 현재 로그인한 사용자의 이메일
        System.out.println("컨트롤러에서 이메일 확인 =" + email);
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member member = optionalMember.orElseThrow(() -> new IllegalArgumentException("해당 이메일로 등록된 사용자가 없습니다: " + email));

        System.out.println("DTO의 Member 필드 확인: " + createQNADto.getMember());

        // DTO에 Member를 설정
        createQNADto.setMember(member);

        myQNAService.save(createQNADto, email);

        return "redirect:/myQNAList";
    }

    //질문게시판 상세 페이지
    @GetMapping("/myQNAView/{id}")
    public String myQNAView(Model model, @PathVariable("id") Long id) {
        System.out.println("컨트롤러에서 받아오는 아이디=" + id);

        Optional<MyQNA> myQNAViewDto = myQNAService.getQNADetail(id);

        if (myQNAViewDto.isPresent()) {
            model.addAttribute("myQNA", myQNAViewDto.get());
        }
        return "myQNA/myQNAView";
    }

    //질문 수정
    @GetMapping("/update/{id}")
    public String updateQNA(@PathVariable("id") Long myQNAId, Model model) {
        Optional<MyQNA> createQNADto = myQNAService.getQNADetail(myQNAId);

        if (createQNADto.isPresent()) {
            model.addAttribute("createQNADto", createQNADto.get());
        }

        return "myQNA/updateQNA";
    }

    @PostMapping("/update/{id}")
    public String updateQNAProc(@PathVariable("id") Long myQNAId,
                                Principal principal,
                                @Valid CreateQNADto createQNADto,
                                BindingResult bindingResult,
                                Model model) throws Exception {

        System.out.println("Writer: " + createQNADto.getWriter());

        if (bindingResult.hasErrors()) {
            return "myQNA/updateQNA"; // 에러가 발생했을 때 다시 입력 폼으로 이동
        }
        try {

            String email = principal.getName();
            Optional<Member> optionalMember = memberRepository.findByEmail(email);
            Member member = optionalMember.orElseThrow(() -> new IllegalArgumentException("해당 이메일로 등록된 사용자가 없습니다: " + email));

            // DTO에 ID 값을 설정
            createQNADto.setId(myQNAId);
            createQNADto.setMember(member);

            // 서비스 호출하여 데이터 업데이트
            myQNAService.save(createQNADto, email);

        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "myQNA/updateQNA";
        }

        return "redirect:/myQNAView/" + myQNAId;
    }

    //질문 삭제
    @Transactional
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        myQNAService.delete(id);
        return "redirect:/myQNAList";
    }

    //관리자가 보는 질문게시판리스트
    @GetMapping("/memberQNAList")
    public String memberQNA(Model model) {
        List<MyQNA> myQNA = myQNAService.findAll();

        model.addAttribute("myQNA", myQNA);
        return "admin/memberQNAList";
    }

    //관리자가 보는 질문 상세
    @GetMapping("/memberQNAView/{id}")
    public String memberQNAView(Model model, @PathVariable("id") Long id) {
        System.out.println("컨트롤러에서 받아오는 아이디=" + id);

        Optional<MyQNA> myQNAViewDto = myQNAService.getQNADetail(id);

        if (myQNAViewDto.isPresent()) {
            model.addAttribute("myQNA", myQNAViewDto.get());
        }
        return "admin/memberQNAView";
    }

    //관리자 답변 등록
    @PostMapping("/submitAnswer")
    public String submitAnswer(
            @RequestParam("reply") String reply,
            @RequestParam("id") Long id,
            RedirectAttributes redirectAttributes
    ) {
        boolean success = myQNAService.saveReply(id, reply);

        // 성공적으로 저장되었는지에 따라 적절한 페이지로 리다이렉트합니다.
        if (success) {
            redirectAttributes.addFlashAttribute("message", "답변이 성공적으로 등록되었습니다.");
            return "redirect:/memberQNAView/" + id; // 리다이렉트할 페이지
        } else {
            redirectAttributes.addFlashAttribute("error", "답변 등록에 실패하였습니다.");
            return "redirect:/memberQNAView/" + id; // 오류 페이지
        }
    }

    //관리자 답변 삭제
    @PostMapping("/deleteAnswer")
    public String deleteAnswer(@RequestParam("id") Long id) {
        boolean success = myQNAService.clearReply(id);

        if(success) {
            return "redirect:/memberQNAList";
        } else{
            return "redirect:/memberQNAView/" + id;
        }

    }
}


