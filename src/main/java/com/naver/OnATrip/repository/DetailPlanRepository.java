package com.naver.OnATrip.repository;

import com.naver.OnATrip.controller.PlanController;
import com.naver.OnATrip.entity.plan.DetailPlan;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityResult;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.naver.OnATrip.entity.plan.QDetailPlan.detailPlan;


@Repository
//@RequiredArgsConstructor
public class DetailPlanRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public DetailPlanRepository(EntityManager em){
        this.em =em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    private static final Logger logger = LoggerFactory.getLogger(DetailPlanRepository.class);

    /*
     * 일자 별 계획 생성
     */
    @Transactional
    public void createDetailPlan(DetailPlan detailPlan) {
        logger.info("DetailPlanRepository-createDetailPlan");
        em.persist(detailPlan);
    }

    public List<DetailPlan> findDetailPlanByPlanId(Long planId){
        logger.info("DetailPlanRepository-findDetailPlanByPlanId");
        return queryFactory
                .selectFrom(detailPlan)
                .where(detailPlan.plan.id.eq(planId))
                .fetch();
    }
}
