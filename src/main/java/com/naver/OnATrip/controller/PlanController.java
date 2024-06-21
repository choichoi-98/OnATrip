package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Plan;
import com.naver.OnATrip.entity.plan.DetailPlan;
import com.naver.OnATrip.service.PlanService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;
    private static final Logger logger = LoggerFactory.getLogger(PlanController.class);

    //이용자가 selectDate에서 일정 선택 후 완료 클릭시 일정 최초 생성
    @PostMapping("/createPlan")
    @ResponseBody
    public String createPlan(Plan plan
                             ,@RequestParam("startDate") String startDate,
                             @RequestParam("endDate") String endDate){
        logger.info("여기는 controller");

        plan.setMemberId(1L);
        plan.setCountry("제주도");
        plan.setStartDate(startDate);
        plan.setEndDate(endDate);
        planService.createPlan(plan);

        return "success";
    }

    //이용자 날짜 선택
    @GetMapping("/selectDate")
    public String selectDate(){

        return "plan/selectDate";
    }

    //세부 계획 생성
    @GetMapping("detailPlan")
    public ModelAndView createDetailPlan(ModelAndView mv,
                                 DetailPlan detailPlan
                                , @RequestParam("dateRange") String dateRange){

        mv.addObject("dateRange",dateRange);

        //(endDate-startDate)계산해서 각 일자 별로 나누는 메서드 필요
        planService.calDate(dateRange);

        System.out.println("============dateRange = " + dateRange);
        mv.setViewName("plan/detailPlan");
        return mv;
    }

}
