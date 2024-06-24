package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.plan.DetailPlan;
import com.naver.OnATrip.repository.DetailPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DetailPlanService {

    private final DetailPlanRepository detailPlanRepository;

    //세부계획 생성
    public void createDetailPlan(DetailPlan detailPlan) {
        detailPlanRepository.createDetailPlan(detailPlan);
    }

    //세부계획 조회
    public List<DetailPlan> findDetailPlanByPlanId(Long planId){
        return detailPlanRepository.findDetailPlanByPlanId(planId);
    }
}
