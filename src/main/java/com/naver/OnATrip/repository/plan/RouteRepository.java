package com.naver.OnATrip.repository.plan;

import com.naver.OnATrip.entity.plan.Route;
//import com.naver.OnATrip.web.dto.plan.RouteDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long>, RouteRepositoryCustom {

}
