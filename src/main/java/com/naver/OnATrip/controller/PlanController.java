package com.naver.OnATrip.controller;

import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
public class PlanController {

    //이용자가 여행도시 클릭시 일정 최초 생성
    @GetMapping("/createPlan")
    public String createPlan(){
        return "plan/createPlan";//-> 수정 필요!
    }

    //이용자 날짜 선택
    @GetMapping("/selectDate")
    public String selectDate(){
        return "plan/selectDate";
    }

    //세부 계획 설정
    @GetMapping("detailPlan")
    public ModelAndView createDetailPlan(ModelAndView mv
                                , @RequestParam("dateRange") String dateRange){

        mv.addObject("dateRange",dateRange);
        System.out.println("============dateRange = " + dateRange);
        mv.setViewName("plan/detailPlan");
        return mv;
    }

}
