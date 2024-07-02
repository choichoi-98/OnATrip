package com.naver.OnATrip.service;

import com.naver.OnATrip.controller.PlanController;
import com.naver.OnATrip.entity.plan.DetailPlan;
import com.naver.OnATrip.entity.plan.Route;
import com.naver.OnATrip.repository.DetailPlanRepository;
//import com.naver.OnATrip.web.dto.plan.RouteDto;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DetailPlanService {

    private final DetailPlanRepository detailPlanRepository;
    private final EntityManager em;
    private static final Logger logger = LoggerFactory.getLogger(DetailPlanService.class);

    @Autowired
    public DetailPlanService(DetailPlanRepository detailPlanRepository, EntityManager em) {
        this.detailPlanRepository = detailPlanRepository;
        this.em = em;
    }
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
