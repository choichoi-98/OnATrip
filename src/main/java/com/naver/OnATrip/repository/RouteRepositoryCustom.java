package com.naver.OnATrip.repository;

import com.naver.OnATrip.constant.RouteCategory;
import com.naver.OnATrip.web.dto.plan.RouteDto;

import java.util.List;
import java.util.Optional;

public interface RouteRepositoryCustom {
    List<RouteDto> findRouteByDetailPlanId(Long detailPlanId);
    Optional<Integer> findMaxRouteSequence(Long detailPlanId, int dayNumber);
    boolean deleteRouteById(Long routeId);
    boolean modifyMemo(Long routeId, String memoContent);

    int countByDetailPlanIdAndCategory(Long detailPlanId, RouteCategory routeCategory);
}
