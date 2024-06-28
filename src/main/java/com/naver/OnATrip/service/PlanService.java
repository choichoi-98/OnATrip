package com.naver.OnATrip.service;

import com.naver.OnATrip.controller.PlanController;
import com.naver.OnATrip.entity.plan.Plan;
import com.naver.OnATrip.repository.PlanRepository;
//import com.naver.OnATrip.web.dto.plan.RouteDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private static final Logger logger = LoggerFactory.getLogger(PlanService.class);

    /*
    * createPlan
    * - PLAN 생성
     */
    @Transactional
    public Long createPlan(Plan plan){
        logger.info("PlanService-createPlan");
        planRepository.createPlan(plan);
        return plan.getId();
    }

    /*
    Plan 객체 조회
    for createDetailPlan
     */
    @Transactional
    public Plan findPlanById(Long planId){
        logger.info("PalnService-findByID");
        return (Plan) planRepository.findPlanById(planId)
                .orElseThrow(()-> new IllegalArgumentException("Plan not found with id: " + planId));
    }

    /*
    여행 기간 계산 및 각 일자 구함
     */
    public Map<String, Object> calDate(String dateRange) {
        logger.info("PlanService-calDate");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        String[] dates = dateRange.split("-");
        LocalDate startDate = LocalDate.parse(dates[0].trim(), formatter);
        LocalDate endDate = LocalDate.parse(dates[1].trim(), formatter);

        long duration = ChronoUnit.DAYS.between(startDate, endDate);

        List<LocalDate> dateList = new ArrayList<>();
        LocalDate date = startDate;
        while (!date.isAfter(endDate)) {
            dateList.add(date);
            date = date.plusDays(1);
        }
        Map<String, Object> result = new HashMap<>();

        result.put("duration", duration);
        result.put("dates", dateList);

        return result;
    }




    /*
    Plan, DetailPlan 함께 저장
     */
//    public void createPlanWithDetails(Plan plan, DetailPlan detailPlan){
//        createPlan(plan);
//
//        detailPlan.setPlan(plan);
//
//        createDetailPlan(detailPlan);
//    }
}
