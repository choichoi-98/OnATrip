package com.naver.OnATrip.service;

import com.naver.OnATrip.controller.PlanController;
import com.naver.OnATrip.entity.plan.DetailPlan;
import com.naver.OnATrip.repository.DetailPlanRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetailPlanService {

    private final DetailPlanRepository detailPlanRepository;
    private static final Logger logger = LoggerFactory.getLogger(DetailPlanService.class);

    //세부계획 생성
    public void createDetailPlan(DetailPlan detailPlan) {
        logger.info("DetailPlanService-createDetailPlan");
        detailPlanRepository.createDetailPlan(detailPlan);
    }

    //세부계획 조회
    public List<DetailPlan> findDetailPlanByPlanId(Long planId){
        logger.info("DetailPlanService-findDetailPlanByPlanId");
        return detailPlanRepository.findDetailPlanByPlanId(planId);
    }
}
