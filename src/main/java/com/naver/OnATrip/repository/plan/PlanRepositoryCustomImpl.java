package com.naver.OnATrip.repository.plan;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.QMember;
import com.naver.OnATrip.entity.pay.QSubscribe;
import com.naver.OnATrip.entity.pay.SubscribeStatus;
import com.naver.OnATrip.entity.plan.LocationProjection;
import com.naver.OnATrip.entity.plan.LocationProjectionImpl;
import com.naver.OnATrip.entity.plan.Plan;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

import static com.naver.OnATrip.entity.QLocation.location;
import static com.naver.OnATrip.entity.QMember.member;
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
                        location.locationType.as("locationType"),
                        location.countryName.as("countryName"),
                        location.countryCode.as("countryCode"),
                        location.city.as("city"),
                        location.image.as("image")))
                .from(location)
                .where(location.id.eq(locationId))
                .fetchOne();
    }

    //Get Plan List
    @Override
    public List<Plan> findBymemberId(String email) {

        return  queryFactory
                .select(plan)
                .from(plan)
                .where(plan.email.eq(email)//email(memberId와 일치 여부)
                        .or(plan.mateId.like("%" + email + "%")))
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



    @Override
    public boolean ExistPlanByEmail(String email, Long planId) {
        logger.info("ExistPlanByEmail planID =" + planId + " email = " + email);
        long count = queryFactory
                .selectFrom(plan)
                .where(
                        plan.id.eq(planId)
                                .and(plan.email.eq(email))
                                .or(plan.id.eq(planId).and(plan.mateId.like("%" + email + "%")))
                )
                .fetchCount();

        return count > 0;
    }

    //친구 초대 시 mateId에 email 값 update
    @Override
    public boolean updateMateEmailToPlan(String email, Long planId) {
        // CASE WHEN 구문을 사용하여 조건부 업데이트를 수행
        StringExpression updatedMateId = new CaseBuilder()
                .when(plan.memberCount.eq(1))
                .then(email)
                .otherwise(plan.mateId.coalesce("").concat(", ").concat(email));

        long updateCount = queryFactory
                .update(plan)
                .set(plan.mateId, updatedMateId)
                .where(plan.id.eq(planId))
                .execute();

        return updateCount > 0;

    }

    //공유된 일정-나가기, 로그인한 유저 = 최초 생성자/ email = null, memberCount--
    @Override
    public void updatePlanEmailAndDecrementMemberCount(Long planId) {
        queryFactory
                .update(plan)
                .set(plan.email, "")
                .set(plan.memberCount, plan.memberCount.subtract(1))
                .where(plan.id.eq(planId))
                .execute();
    }

    //공유된 일정-나가기, 로그인한 유저= 초대 받은 멤버/ mateId -> update, memberCount--
    @Override
    public void updatePlanMateIdAndDecrementMemberCount(Long planId, String updatedMateId) {
        queryFactory
                .update(plan)
                .set(plan.mateId, updatedMateId)
                .set(plan.memberCount, plan.memberCount.subtract(1))
                .where(plan.id.eq(planId))
                .execute();
    }

    //여행 목록 5개 조회
    @Override
    public List<Plan> findFivePlan(String email) {
        return queryFactory
                .selectFrom(plan)
                .where(plan.email.eq(email))
                .limit(5)
                .fetch();
    }

    @Override
    public String getStatusByEmail(String email) {
        String subscribeStatus = queryFactory
                .select(member.subscribe_status)
                .from(member)
                .where(member.email.eq(email))
                .fetchOne();
        return subscribeStatus;
    }
}
