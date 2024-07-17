package com.naver.OnATrip.repository;

import com.naver.OnATrip.entity.plan.LocationProjection;


public interface PlanRepositoryCustom {

    LocationProjection findLocationById(Long locationId);
}
