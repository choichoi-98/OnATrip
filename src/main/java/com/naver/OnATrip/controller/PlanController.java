package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.plan.Plan;
import com.naver.OnATrip.entity.plan.DetailPlan;
import com.naver.OnATrip.service.DetailPlanService;
import com.naver.OnATrip.service.PlanService;
import com.naver.OnATrip.web.dto.plan.RouteDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;
    private final DetailPlanService detailPlanService;
    private static final Logger logger = LoggerFactory.getLogger(PlanController.class);

    //dto 객체 이용 createPlan
    @PostMapping("/createPlan")
    @ResponseBody
    public String createPlan(Plan plan
            ,@RequestParam("startDate") String startDate,
                             @RequestParam("endDate") String endDate){
        logger.info("PlanController-createPlan");

        plan.setMemberId(1L); //->회원가입 및 로그인 완료 후 수정 필요
        plan.setCountry("태국");//->관리자 국가/도시 추가 후 수정 필요
        plan.setStartDate(startDate);
        logger.info(startDate);
        plan.setEndDate(endDate);
        Long planId = planService.createPlan(plan);

        return String.valueOf(planId);
    }

    //이용자 날짜 선택
    @GetMapping("/selectDate")
    public String selectDate(){
        logger.info("PlanController-selectDate");
        return "plan/selectDate";
    }

    //세부 계획 생성
    @GetMapping("detailPlan")
    public ModelAndView createDetailPlan(ModelAndView mv,
                                 @RequestParam("planId") Long planId,
                                 @RequestParam("dateRange") String dateRange){

        Plan plan = planService.findPlanById(planId);
        logger.info("PlanController-createDetailPlan");

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

        mv.addObject("detailPlans", detailPlans);
        mv.setViewName("plan/detailPlan");
        return mv;
    }

    //일자별 route 생성
    @PostMapping("addRoute")
    public ResponseEntity<?> addRoute(@RequestBody RouteDto routeData){
        logger.info("addRoute의 Received day: " + routeData.getDay());
        logger.info("addRoute의 placeName: " + routeData.getPlaceName());
        logger.info("addRoute의 category: " + routeData.getCategory());
        logger.info("addRoute의 detailPlanId: " + routeData.getDetailPlan_id());

        detailPlanService.addRoute(routeData);

        //test 응답 데이터
        Map<String,String> response = new HashMap<>();
        response.put("test","test-val");

        return ResponseEntity.ok(response);

    }
}
