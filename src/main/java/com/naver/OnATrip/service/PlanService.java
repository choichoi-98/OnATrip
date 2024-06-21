package com.naver.OnATrip.service;

import com.naver.OnATrip.controller.PlanController;
import com.naver.OnATrip.entity.Plan;
import com.naver.OnATrip.entity.plan.DetailPlan;
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

    /*
    여행 기간 계산 및 각 일자 구함
     */
    public String calDate(String dateRange) {
        String[] dates = dateRange.split("-");
        String startDate = dates[0];
        String endDate = dates[1];


        return null;
    }

    /*
    * 일자 별 계획 생성
     */

    public void createDetailPlan(DetailPlan detailPlan) {
        planRepository.createDetailPlan(detailPlan);
    }

}
