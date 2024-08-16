package com.naver.OnATrip.repository.plan;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.pay.SubscribeStatus;
import com.naver.OnATrip.entity.plan.LocationProjection;
import com.naver.OnATrip.entity.plan.Plan;

import java.util.List;


public interface PlanRepositoryCustom {

    LocationProjection findLocationById(Long locationId);

    List<Plan> findBymemberId(String email);

    boolean deletePlanById(Long planId);

    String getStatusByEmail(String email);

    boolean ExistPlanByEmail(String email, Long planId);

    boolean updateMateEmailToPlan(String email, Long planId);

    void updatePlanEmailAndDecrementMemberCount(Long planId);

    void updatePlanMateIdAndDecrementMemberCount(Long planId, String updatedMateId);

    List<Plan> findFivePlan(String email);

//    Member findMemberByEmail(String email);
}
