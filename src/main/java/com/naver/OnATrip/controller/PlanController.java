package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.plan.LocationProjection;
import com.naver.OnATrip.entity.plan.Plan;
import com.naver.OnATrip.entity.plan.DetailPlan;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PlanController {
    private final PlanService planService;
    private final DetailPlanService detailPlanService;
    private final RouteService routeService;
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
    public String selectDate(Model model){
        Long locationId = 1L; // 예시로 1L로 설정
        //-> 이거 나중에 연결하면 ok~

        logger.info("PlanController-selectDate");

        // Model 객체에 데이터 추가
        model.addAttribute("locationId", locationId);

        return "plan/selectDate"; // 해당하는 HTML 파일의 경로와 파일명을 반환
    }

    //Plan 생성
    @PostMapping("/createPlan")
    @ResponseBody
    public String createPlan(@RequestBody PlanDto planDto) {
        logger.info("PlanController-createPlan");


        // Plan 생성
        Long planId = planService.createPlan(planDto);

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

        //여행 일자 계산
        List<LocalDate> datesList = planService.calculateDates(startDate, endDate);

        //필요한 location 객체의 정보 저장
        LocationProjection location = planService.findLocationById(plan.getLocation().getId());

        for (LocalDate date : datesList) {
            DetailPlanDto detailPlanDto = DetailPlanDto.builder()
                    .planId(planDto.getId())
                    .memberId(planDto.getMemberId())
                    .countryName(location.getCountryName())
                    .countryCode(location.getCountryCode())
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
        logger.info("Controller------------viewDetailPlan 0----------------");
        List<DetailPlan> detailPlans = detailPlanService.findDetailPlanByPlanId(planId);
        Map<Long, List <RouteDto>> routeMap = new HashMap<>();
        for (DetailPlan dp : detailPlans) {
            logger.info("DetailPlan ID: {}, Country: {}, PerDate: {}", dp.getId(), dp.getCountryName(), dp.getCountryCode() , dp.getPerDate());
            List<RouteDto> routes = routeService.findRoutesByDetailPlanId(dp.getId());
            for (RouteDto route : routes) {
                logger.info("RouteDto Id: {}, category: {}, day_number: {}, place_name: {}, sort_key: {}, mark_seq: {}", route.getId(), route.getCategory(), route.getDay_number(), route.getPlaceName(), route.getSortKey(), route.getMarkSeq());
            }
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
        logger.info("Received route data: " + routeData.toString());
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

    //modifyMemo-메모 내용 수정
    @PostMapping("/modifyMemo")
    @ResponseBody
    public Map<String, String> modifyMemo(@RequestParam("modifyRouteId") Long routeId,
                           @RequestParam("updatedMemoContent") String memoContent){

        boolean isUpdated = routeService.modifyMemo(routeId, memoContent);

        Map<String, String> response = new HashMap<>();
        response.put("status", isUpdated ? "success": "fail");

        return  response;
    }

//    routeSequence update
    @PostMapping("/updateRouteSequence")
    @ResponseBody
    public ResponseEntity<String> updateRouteSequence(@RequestBody List<RouteDto> routeDtos) {
        logger.info("controller - updateRouteSequence---------------------");

        for (RouteDto routeDto : routeDtos) {
            logger.info("RouteDto: {}", routeDto.toString());
        }

        boolean result = routeService.updateRouteSequence(routeDtos);

        if (result) {
            return ResponseEntity.ok("Route sequence updated successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to update route sequence");
        }
    }

    //myPage 이동
    @GetMapping("/myPage")
    public String myPage(@AuthenticationPrincipal Principal principal){

        logger.info("Principal Object: {}", principal);

//        logger.info("Principal",principal.getName());

        return "/myPage";
    }

}//public class PlanController {

