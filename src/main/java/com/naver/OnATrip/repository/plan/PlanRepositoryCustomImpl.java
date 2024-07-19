package com.naver.OnATrip.repository.plan;

import com.naver.OnATrip.entity.plan.LocationProjection;
import com.naver.OnATrip.entity.plan.LocationProjectionImpl;
import com.naver.OnATrip.entity.plan.Plan;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static com.naver.OnATrip.entity.QLocation.location;
import static com.naver.OnATrip.entity.plan.QPlan.plan;


public class PlanRepositoryCustomImpl implements PlanRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PlanRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    private static final Logger logger = LoggerFactory.getLogger(PlanRepositoryCustomImpl.class);


    //locationId값을 이용해 필요한 location 도메인의 정보를 locationProjection 형식으로 반환
    @Override
    public LocationProjection findLocationById(Long locationId) {
        return queryFactory
                .select(Projections.bean(LocationProjectionImpl.class,
                        location.id.as("id"),
                        location.countryName.as("countryName"),
                        location.countryCode.as("countryCode"),
                        location.image.as("image")))
                .from(location)
                .where(location.id.eq(locationId))
                .fetchOne();
    }

    @Override
    public List<Plan> findBymemberId(String email) {

        return  queryFactory
                .select(plan)
                .from(plan)
                .where(plan.email.eq(email))
                .fetch();
    }

    @Override
    @Transactional
    public boolean deletePlanById(Long planId) {
        long deletedCount = queryFactory
                .delete(plan)
                .where(plan.id.eq(planId))
                .execute();

        return deletedCount > 0;
    }
}
