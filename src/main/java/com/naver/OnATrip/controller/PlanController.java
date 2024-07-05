package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.plan.Plan;
import com.naver.OnATrip.entity.plan.DetailPlan;
import com.naver.OnATrip.entity.plan.Route;
import com.naver.OnATrip.service.DetailPlanService;
import com.naver.OnATrip.service.PlanService;
import com.naver.OnATrip.service.RouteService;
import com.naver.OnATrip.web.dto.plan.DetailPlanDto;
import com.naver.OnATrip.web.dto.plan.PlanDto;
import com.naver.OnATrip.web.dto.plan.RouteDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
//@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;
    private final DetailPlanService detailPlanService;
    private RouteService routeService;
    private static final Logger logger = LoggerFactory.getLogger(PlanController.class);

    @Autowired
    public PlanController(PlanService planService, DetailPlanService detailPlanService, RouteService routeService) {

        this.planService = planService;
        this.detailPlanService = detailPlanService;
        this.routeService = routeService;
    }

    // GET 요청 처리 메서드
    //이용자 날짜 선택
    @GetMapping("/selectDate")
    public String selectDate(){
        logger.info("PlanController-selectDate");
        return "plan/selectDate";
    }

    //Plan 생성
    @PostMapping("/createPlan")
    @ResponseBody
    public String createPlan(@RequestBody PlanDto planDto){
        logger.info("PlanController-createPlan");
        //planDto -> Plan entity
        Plan plan = planDto.toEntity();
        Long planId = planService.createPlan(plan);

        return String.valueOf(planId);
    }

    //detailPlan 생성
    @PostMapping("/createDetailPlan")
    public ResponseEntity<Map<String, Long>> createDetailPlan(
            @RequestBody Map<String, String> request) {
        String dateRange = request.get("dateRange").trim();
        Long planId = Long.parseLong(request.get("planId"));

        Plan plan = planService.findPlanById(planId);
        PlanDto planDto = new PlanDto(plan);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        // 공백 제거 및 날짜 형식 지정
        String[] dates = dateRange.split("-");
        LocalDate startDate = LocalDate.parse(dates[0].trim(), formatter);
        LocalDate endDate = LocalDate.parse(dates[1].trim(), formatter);

        List<LocalDate> datesList = planService.calculateDates(startDate, endDate);

        for (LocalDate date : datesList) {
            DetailPlanDto detailPlanDto = DetailPlanDto.builder()
                    .planId(planDto.getId())
                    .memberId(planDto.getMemberId())
                    .country(planDto.getCountry())
                    .perDate(date)
                    .build();

            detailPlanService.createDetailPlan(detailPlanDto.toEntity());
        }

        Map<String, Long> response = new HashMap<>();
        response.put("planId", planId);

        return ResponseEntity.ok(response);
    }

    //detailPlan, route 조회 - detailPlan HTML 뷰 랜더링
    @GetMapping("/viewDetailPlan")
    public ModelAndView detailPlan(ModelAndView mv,
                                   @RequestParam("planId") Long planId
                                   ) {

        List<DetailPlan> detailPlans = detailPlanService.findDetailPlanByPlanId(planId);
        Map<Long, List <RouteDto>> routeMap = new HashMap<>();
        for (DetailPlan dp : detailPlans) {
            logger.info("DetailPlan ID: {}, Country: {}, PerDate: {}", dp.getId(), dp.getCountry(), dp.getPerDate());
            List<RouteDto> routes = routeService.findRoutesByDetailPlanId(dp.getId());
            routeMap.put(dp.getId(),routes);
        }

        mv.addObject("detailPlans", detailPlans);
        mv.addObject("routesMap", routeMap);
        mv.setViewName("plan/detailPlan"); // View 이름 설정
        return mv;
    }

    //일자별 route 생성
    @PostMapping("addRoute")
    public ResponseEntity<?> addRoute(@RequestBody RouteDto routeData){
        logger.info("planController-addRoute");

        RouteDto newRoute = routeService.addRoute(routeData);

        return ResponseEntity.ok(newRoute);

    }

    //ajax - viewDetailPlan, route 삽입 시 요청 됨.
    @GetMapping("/api/viewRoute")
    public ResponseEntity<?> detailPlanJson(@RequestParam("detailPlanId") Long detailPlanId){

        List<RouteDto> routes = routeService.findRoutesByDetailPlanId(detailPlanId);

        logger.info("Routes for detailPlanId {} : {} ", detailPlanId, routes);

        return ResponseEntity.ok(routes);
    }


    //routeDelete
    @PostMapping("/deleteRoute")
    public ResponseEntity<Boolean> deleteRoute(@RequestParam ("routeId") Long routeId){
        logger.info("deleteRoute-routeId", routeId);
        boolean result = routeService.deleteRoute(routeId);
        if (result) {
            return ResponseEntity.ok(result);  // 성공 시 200 OK 응답
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);  // 실패 시 500 응답
        }
    }
}
