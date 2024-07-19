package com.naver.OnATrip.repository.plan;

import com.naver.OnATrip.constant.RouteCategory;
import com.naver.OnATrip.entity.plan.Route;
import com.naver.OnATrip.web.dto.plan.RouteDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.naver.OnATrip.entity.plan.QRoute.route;

public class RouteRepositoryCustomImpl implements RouteRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public RouteRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    @Transactional
    public List<RouteDto> findRouteByDetailPlanId(Long detailPlanId) {
        List<Route> routes = queryFactory
                .selectFrom(route)
                .where(route.detailPlan.id.eq(detailPlanId))
                .orderBy(route.routeSequence.asc())
                .fetch();

        return routes.stream()
                .map(RouteDto::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<Integer> findMaxRouteSequence(Long detailPlanId, int dayNumber) {
        Integer maxSequence = queryFactory
                .select(route.routeSequence.max())
                .from(route)
                .where(route.detailPlan.id.eq(detailPlanId)
                        .and(route.day_number.eq(dayNumber)))
                .fetchOne();

        return Optional.ofNullable(maxSequence);
    }

    @Override
    @Transactional
    public boolean deleteRouteById(Long routeId) {
        long deletedCount = queryFactory
                .delete(route)
                .where(route.id.eq(routeId))
                .execute();

        return deletedCount > 0;
    }

    @Override
    @Transactional
    public boolean modifyMemo(Long routeId, String memoContent) {
        long updatedCount = queryFactory.update(route)
                .set(route.memo, memoContent)
                .where(route.id.eq(routeId))
                .execute();

        return updatedCount > 0;
    }

    @Override
    public int countByDetailPlanIdAndCategory(Long detailPlanId, RouteCategory routeCategory) {
        return (int) queryFactory
                .select(route.count())
                .from(route)
                .where(route.detailPlan.id.eq(detailPlanId).
                        and(route.category.eq(routeCategory)))
                .fetchCount();
    }


}
