package com.naver.OnATrip.repository;

import com.naver.OnATrip.controller.PlanController;
import com.naver.OnATrip.entity.Plan;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class PlanRepository {

    private final EntityManager em;
    private static final Logger logger = LoggerFactory.getLogger(PlanController.class);


    //계획 최초 생성
    @Transactional
    public void createPlan(Plan plan) {
        logger.info("여기는 레포지토리");
        em.persist(plan);
    }
}
