package com.naver.OnATrip.repository;

import com.naver.OnATrip.controller.PlanController;
import com.naver.OnATrip.entity.Plan;
import com.naver.OnATrip.entity.plan.DetailPlan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PlanRepository {

    private final EntityManager em;


    //계획 생성
    @Transactional
    public void createPlan(Plan plan) {
        em.persist(plan);
    }

    //세부 계획 생성
    @Transactional
    public void createDetailPlan(DetailPlan detailPlan) {
        em.persist(detailPlan);
    }

    //Plan 객체 Id로 조회
    @Transactional
    public Optional<Object> findPlanById(Long planId) {
        try {
            return Optional.ofNullable(em.find(Plan.class, planId));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
