package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.pay.Subscribe;
import com.naver.OnATrip.entity.plan.DetailPlan;
import com.naver.OnATrip.entity.plan.LocationProjection;
import com.naver.OnATrip.entity.plan.Plan;
import com.naver.OnATrip.repository.MemberRepository;
import com.naver.OnATrip.service.*;
import com.naver.OnATrip.web.dto.plan.DetailPlanDto;
import com.naver.OnATrip.web.dto.plan.PlanDto;
import com.naver.OnATrip.web.dto.plan.RouteDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class PlanController {
    private final PlanService planService;
    private final DetailPlanService detailPlanService;
    private final RouteService routeService;
    private final MemberService memberService;
    private final OrderService orderService;
    private static final Logger logger = LoggerFactory.getLogger(PlanController.class);


    @Autowired
    public PlanController(PlanService planService, DetailPlanService detailPlanService, RouteService routeService, MemberRepository memberRepository, MemberService memberService, OrderService orderService, RestTemplate restTemplate) {
        this.planService = planService;
        this.detailPlanService = detailPlanService;
        this.routeService = routeService;
        this.memberService = memberService;
        this.orderService = orderService;
        this.restTemplate = restTemplate;
    }

    @Value("${google.maps.api-key}")
    private String googleMapsApiKey;

    private final RestTemplate restTemplate;

    // Google Maps API 호출 메서드
    private String getMapData(String location) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=" + googleMapsApiKey;
        return restTemplate.getForObject(url, String.class);
    }

    // GET 요청 처리 메서드
    //이용자 날짜 선택
    @GetMapping("/selectDate")
    public ModelAndView selectDate(ModelAndView mv, Principal principal, @RequestParam("locationId") Long locationId){


        String email = principal.getName();
        //-------null이면 어쩌구~~~
        logger.info("PlanController-selectDate");


        // Model 객체에 데이터 추가
        mv.addObject("locationId", locationId);
        mv.addObject("email", email);
        mv.setViewName("plan/selectDate"); // View 이름 설정

        //"plan/selectDate"
        return mv;
    }

    //Plan 생성
    @PostMapping("/createPlan")
    @ResponseBody
    public String createPlan(@RequestBody PlanDto planDto) {
        logger.info("PlanController-createPlan");
        String email = planDto.getEmail();

        Long planCount = planService.planCount(email);
        System.out.println("planCount = " + planCount);
        //-----------------------------나중에 수정
//        if (planCount > 3 ){
//            String msg = "planCount > 3 이상";
//            SubscribeStatus status = planService.getSubscribeStatus(email);
//            System.out.println("planCount = " + planCount);
//            System.out.println("status = " + status);
//            return msg;
//        } else {
//
//        }

            Long planId = planService.createPlan(planDto);
            return String.valueOf(planId);
        // Plan 생성

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
                    .email(planDto.getEmail())
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
        logger.info("Controller------------viewDetailPlan----------------");
        List<DetailPlan> detailPlans = detailPlanService.findDetailPlanByPlanId(planId);

        // 첫 번째 DetailPlan의 위치 정보를 사용하여 Google Maps API 호출
        if (!detailPlans.isEmpty()) {
            DetailPlan firstDetailPlan = detailPlans.get(0);
            String locationType = firstDetailPlan.getPlan().getLocation().getLocationType(); // 위치 타입

            String latlng = "";
            if ("domestic".equals(locationType)) {
                latlng = ""; // 빈 문자열로 설정
            } else {
                String countryCode = firstDetailPlan.getPlan().getLocation().getCountryCode(); // 국가 코드
                mv.addObject("countryCode", countryCode); // 국가 코드를 모델에 추가
            }

            mv.addObject("latlng", latlng); // latlng을 모델에 추가
        }

        Map<Long, List <RouteDto>> routeMap = new HashMap<>();
        for (DetailPlan dp : detailPlans) {
            logger.info("DetailPlan ID: {}, Country: {}, CountryCode: {}, PerDate: {}, Email: {}, Location locationType: {},  Location city: {}",
                    dp.getId(), dp.getCountryName(), dp.getCountryCode(), dp.getPerDate(), dp.getEmail()
                    ,dp.getPlan().getLocation().getLocationType()
                    ,dp.getPlan().getLocation().getCity());
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

    //Plan 나가기
    @PostMapping("/deletePlan")
    public ResponseEntity<Boolean> deletePlan(@RequestParam("planId") Long planId, Principal principal){
        String email = principal.getName();
        logger.info("--------------deletePlan - planId : " + planId + " email : " + email);
        boolean result = planService.deleteOrUpdatePlan(planId,email);
        if(result){
            return ResponseEntity.ok((result));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    //Plan 리스트 반환 정적 + 동적
    @GetMapping("/getPlans")
    public String getPlans(Model model, Principal principal,
                           @RequestParam(value = "activeTabId", defaultValue = "private-tab") String activeTabId){
        String email = principal.getName();
        List<Plan> plans = planService.findPlanBymemberId(email);
        model.addAttribute("plans", plans);

        if ("private-tab".equals(activeTabId)) {
            return "fragments/planList :: planListContent";
        } else {
            return "fragments/sharedPlanList :: SharedPlanListContent";
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

    //내 여행 목록으로 이동
    @GetMapping("/myPlanList")
    public ModelAndView myPlanList(
                               ModelAndView mv,
                               Principal principal){


        String email = principal.getName();

        List<Plan> plans = planService.findPlanBymemberId(email);
        mv.addObject("plans", plans);
        mv.setViewName("/myPlanList");

        return mv;
    }

    //친구 이메일로 검색
    @GetMapping("/searchFriendByEmail")
    public ResponseEntity<Boolean> searchFriendByEmail(@RequestParam("email") String email){
         Boolean result = planService.existsByEmail(email);

        System.out.println("existEmail result = " + result);

        return ResponseEntity.ok(result);
    }

    //친구 초대
    @PostMapping("/inviteFriend")
    public ResponseEntity<String> inviteFriend(@RequestParam("email") String email,
                                               @RequestParam("planId") Long planId){
        //1. mateEmail 혹은 email에 매개변수로 받은 email과 일치하는 값이 있는지 확인
        boolean result = planService.ExistPlanByEmail(email, planId);
        System.out.println("친구 초대 이메일 Exist result = " + result);

        if(result){//1-1. 있다면 '이미 초대된 친구입니다.' 띄우기

            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 초대된 친구입니다.");

        }else {//2. 없는 경우 mateEmail에 email 값 넣기

            boolean updateResult = planService.updateMateEmailToPlan(email, planId);
            System.out.println("updateMateEmail-----" +  updateResult);

            if(updateResult){
                //3. memberCount 증가시키기
                boolean incrementResult = planService.incrementMemberCount(planId);
                if(incrementResult){
                    return ResponseEntity.ok("친구 초대 성공");
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("멤버 카운트 증가 중 오류 발생");
                }
            }else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("친구 초대 중 오류 발생");
            }
        }

    }

    @GetMapping("/myPage")
    public ModelAndView myPage(ModelAndView mv, Principal principal){

        String email = principal.getName();

        //최대 다섯개만 가져오기
        List<Plan> plans = planService.findFivePlanBymemberId(email);

        mv.addObject("plans", plans);

        Member member = memberService.findByEmail(email);
        mv.addObject("member", member);


//        Optional<Subscribe> subscribe = orderService.findByMemberId(email);
//        if (subscribe.isPresent()){
//            mv.addObject("subscribe", subscribe);
//        } else {
//            mv.addObject("subscribe", null);
//        }

        mv.setViewName("myPage");
        return mv;
    }

}//public class PlanController {

