package com.naver.OnATrip.repository;

import com.naver.OnATrip.entity.plan.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long>, PlanRepositoryCustom {
}
