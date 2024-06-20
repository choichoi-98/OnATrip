package com.naver.OnATrip.service;

import com.naver.OnATrip.controller.PlanController;
import com.naver.OnATrip.entity.Plan;
import com.naver.OnATrip.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;
    private static final Logger logger = LoggerFactory.getLogger(PlanController.class);

    /*
    * createPlan
    * - 일정 최초 생성
     */
    public Long createPlan(Plan plan){
        logger.info("여기는 서비스");
        planRepository.createPlan(plan);
        return plan.getId();
    }
}
