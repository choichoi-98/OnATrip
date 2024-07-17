package com.naver.OnATrip.service;

import com.naver.OnATrip.controller.PlanController;
import com.naver.OnATrip.entity.Location;
import com.naver.OnATrip.entity.plan.LocationProjection;
import com.naver.OnATrip.entity.plan.Plan;
import com.naver.OnATrip.repository.PlanRepository;
//import com.naver.OnATrip.web.dto.plan.RouteDto;
import com.naver.OnATrip.web.dto.plan.PlanDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
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
public class PlanService {

    private final PlanRepository planRepository;
    private final EntityManager em;
    private static final Logger logger = LoggerFactory.getLogger(PlanService.class);

    @Autowired
    public PlanService(PlanRepository planRepository, EntityManager em) {
        this.planRepository = planRepository;
        this.em = em;
    }

    @Transactional
    public Long createPlan(PlanDto requestDto) {
        logger.info("PlanService-createPlan");


        LocationProjection locationProjection = planRepository.findLocationById(requestDto.getLocationId());


        // Plan 생성
        Plan plan = requestDto.toEntity(locationProjection);

        // Plan 저장
        planRepository.save(plan);
        return plan.getId();
    }

    public LocationProjection findLocationById(Long locationId) {
        // LocationProjection을 locationId로 조회하는 로직
        return planRepository.findLocationById(locationId);
    }

    //plan 객체 조회
    @Transactional(readOnly = true)
    public Plan findPlanById(Long planId) {
        logger.info("PlanService-findPlanById");
        return planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found with id: " + planId));
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

    public List<LocalDate> calculateDates(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate currentDate = startDate;

        while (!currentDate.isAfter(endDate)) {
            dates.add(currentDate);
            currentDate = currentDate.plusDays(1);
        }

        return dates;
    }


}
