package com.naver.OnATrip.service;

import com.naver.OnATrip.constant.RouteCategory;
import com.naver.OnATrip.entity.plan.Route;
import com.naver.OnATrip.repository.RouteRepository;
import com.naver.OnATrip.web.dto.plan.RouteDto;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteService {

    private RouteRepository routeRepository;
    private final EntityManager em;
    private static final Logger logger = LoggerFactory.getLogger(RouteService.class);

    @Autowired
    public RouteService(EntityManager em, RouteRepository routeRepository) {
        this.em = em;
        this.routeRepository = routeRepository;

    }

    public RouteDto addRoute(RouteDto routeDto){
        Long detailPlanId = routeDto.getDetailPlan_id();
        int dayNumber = routeDto.getDay_number();
        logger.info("findMaxRouteSequence 매개변수 - detailPlanId: {}, dayNumber: {}", detailPlanId, dayNumber);

        // 현재 day_number에 대한 routeSequence 최댓값
        Optional<Integer> maxRouteSequence = routeRepository.findMaxRouteSequence( detailPlanId, dayNumber);

        // 새로운 routeSequence 값 설정
        int newRouteSequence = maxRouteSequence.orElse(0) + 1;
        routeDto.setRouteSequence(newRouteSequence);

        //calMaxSeq
        int newMarkSeq = calMaxMarkSeq(detailPlanId);
        routeDto.setMarkSeq(newMarkSeq);

        logger.info("RouteService-addRoute - DTO: {}", routeDto);
        Route route = routeDto.toEntity();//dto-> entity로 변환

        Route saveRoute = routeRepository.save(route);
        logger.info("RouteService-addRoute - Saved Route: {}", saveRoute);

        return new RouteDto(saveRoute);
    }

    public int calMaxMarkSeq(Long detailPlanId){
        int placeBlockCount = routeRepository.countByDetailPlanIdAndCategory(detailPlanId, RouteCategory.PLACE);
        logger.info("calMaxMarkSeq",String.valueOf(placeBlockCount));
        int newMarkSeq = placeBlockCount + 1;

        return  newMarkSeq;
    }


    public List<RouteDto> findRoutesByDetailPlanId(Long detailPlanId){
        logger.info("RouteService-findRoutesByDetailPlanId - DetailPlan ID: {}", detailPlanId);
        return routeRepository.findRouteByDetailPlanId(detailPlanId);
    }

    public boolean deleteRoute(Long routeId) {
        logger.info("RouteService-deleteRoute - routeId", routeId);
        return routeRepository.deleteRouteById(routeId);
    }

    public boolean modifyMemo(Long routeId, String memoContent) {
        logger.info("RouteService-modifyMemo- routeId ", routeId, memoContent);
        return routeRepository.modifyMemo(routeId, memoContent);
    }

    public boolean updateRouteSequence(List<RouteDto> routeDtos) {
        try {
            for (RouteDto routeDto : routeDtos) {
                Route route = routeRepository.findById(routeDto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("Invalid route ID: " + routeDto.getId()));
                route.updateRouteSequence(routeDto.getRouteSequence());
                routeRepository.save(route); // Save the updated entity
            }
            return true;
        } catch (Exception e) {
            logger.error("Error updating route sequences", e);
            return false;
        }
    }



}
