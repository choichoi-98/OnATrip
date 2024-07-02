package com.naver.OnATrip.repository;

import com.naver.OnATrip.entity.plan.Route;
//import com.naver.OnATrip.web.dto.plan.RouteDto;
import com.naver.OnATrip.web.dto.plan.RouteDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.naver.OnATrip.entity.plan.QRoute.route;

@Repository
public class RouteRepository {

//    List<Route> findByDetailPlan_Id(Long detailPlanId); // detailPlan의 id로 route를 조회하는 메서드 선언

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;


    public RouteRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    private static final Logger logger = LoggerFactory.getLogger(DetailPlanRepository.class);

    @Transactional
    public Route addRoute(Route route){
        if (route.getId() == null) {
            em.persist(route);
            em.flush(); // 데이터베이스와 동기화
            return route; // persist된 엔티티 반환
        } else {
            throw new IllegalArgumentException("Route entity is not new. ID should be null.");
        }
    }

    //route 조회
    public List<RouteDto> findRouteByDetailPlanId(Long detailPlanId){
        logger.info("RouteRepository-findRouteByDetailPlanId:  ", detailPlanId);

        List<Route> routes = queryFactory
                .selectFrom(route)
                .where(route.detailPlan.id.eq(detailPlanId))
                .fetch();

        return routes.stream()
                .map(RouteDto::new)
                .collect(Collectors.toList());
    }

    //routeSequence 최대값 반환
    public Optional<Integer> findMaxRouteSequence(Long detailPlanId, int dayNumber){
        Integer maxSequence = queryFactory
                .select(route.routeSequence.max())
                .from(route)
                .where(route.detailPlan.id.eq(detailPlanId)
                        .and(route.day_number.eq(dayNumber)))
                .fetchOne();

        return Optional.ofNullable(maxSequence);

    }

}
