package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Plan;
import com.naver.OnATrip.entity.plan.DetailPlan;
import com.naver.OnATrip.service.DetailPlanService;
import com.naver.OnATrip.service.PlanService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;
    private final DetailPlanService detailPlanService;
    private static final Logger logger = LoggerFactory.getLogger(PlanController.class);

    //이용자가 selectDate에서 일정 선택 후 완료 클릭시 일정 최초 생성
//    @PostMapping("/createPlan")
//    @ResponseBody
//    public String createPlan(Plan plan
//                             ,@RequestParam("startDate") String startDate,
//                             @RequestParam("endDate") String endDate){
//        logger.info("-createController");
//
//        plan.setMemberId(1L);
//        plan.setCountry("제주도");
//        plan.setStartDate(startDate);
//        plan.setEndDate(endDate);
//        Long planId = planService.createPlan(plan);
//
//        return String.valueOf(planId);
//    }

    //dto 객체 이용 createPlan
    @PostMapping("/createPlan")
    @ResponseBody
    public String createPlan(Plan plan
            ,@RequestParam("startDate") String startDate,
                             @RequestParam("endDate") String endDate){
        logger.info("-createController");

        plan.setMemberId(1L);
        plan.setCountry("제주도");
        plan.setStartDate(startDate);
        plan.setEndDate(endDate);
        Long planId = planService.createPlan(plan);

        return String.valueOf(planId);
    }

    //이용자 날짜 선택
    @GetMapping("/selectDate")
    public String selectDate(){

        return "plan/selectDate";
    }

    //세부 계획 생성
    @GetMapping("detailPlan")
    public ModelAndView createDetailPlan(ModelAndView mv,
                                 @RequestParam("planId") Long planId,
                                 @RequestParam("dateRange") String dateRange){

        Plan plan = planService.findPlanById(planId);

        Map<String, Object> calDate = planService.calDate(dateRange);

        List<LocalDate> dates = (List<LocalDate>) calDate.get("dates");
        for (LocalDate date : dates) {
            DetailPlan detailPlan = new DetailPlan();
            detailPlan.setMemberId(plan.getMemberId());
            detailPlan.setPlan(plan);
            detailPlan.setCountry(plan.getCountry());
            detailPlan.setPerDate(date);
            detailPlanService.createDetailPlan(detailPlan);
        }

        List<DetailPlan> detailPlans = detailPlanService.findDetailPlanByPlanId(planId);
        for (DetailPlan dp : detailPlans) {
            logger.info("DetailPlan ID: {}, Country: {}, PerDate: {}", dp.getId(), dp.getCountry(), dp.getPerDate());
        }

//        mv.addObject("dateRange",dateRange);
//        mv.addObject("duration", calDate.get("duration"));
//        mv.addObject("dates", calDate.get("dates"));
        mv.addObject("detailPlans", detailPlans);
        mv.setViewName("plan/detailPlan");
        return mv;
    }

    //

}
