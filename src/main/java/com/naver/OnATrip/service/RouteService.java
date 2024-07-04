package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.plan.Route;
import com.naver.OnATrip.repository.RouteRepository;
import com.naver.OnATrip.web.dto.plan.RouteDto;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        logger.info("RouteService-addRoute");

        // 현재 day_number에 대한 routeSequence 최댓값
        Optional<Integer> maxRouteSequence = routeRepository.findMaxRouteSequence(routeDto.getDetailPlan_id(), routeDto.getDay_number());

        // 새로운 routeSequence 값 설정
        int newRouteSequence = maxRouteSequence.orElse(0) + 1;
        routeDto.setRouteSequence(newRouteSequence);

        logger.info("RouteService-addRoute - DTO: {}", routeDto);
        Route route = routeDto.toEntity();//dto-> entity로 변환


        Route saveRoute = routeRepository.addRoute(route);
        logger.info("RouteService-addRoute - Saved Route: {}", saveRoute);

        return new RouteDto(saveRoute);
    }



    public List<RouteDto> findRoutesByDetailPlanId(Long detailPlanId){
        logger.info("RouteService-findRoutesByDetailPlanId - DetailPlan ID: {}", detailPlanId);
        return routeRepository.findRouteByDetailPlanId(detailPlanId);
    }

    public boolean deleteRoute(Long routeId) {
        return routeRepository.deleteRouteById(routeId);
    }
}
