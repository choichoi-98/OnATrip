package com.naver.OnATrip.repository;

import com.naver.OnATrip.constant.RouteCategory;
import com.naver.OnATrip.entity.plan.Route;
//import com.naver.OnATrip.web.dto.plan.RouteDto;
import com.naver.OnATrip.web.dto.plan.RouteDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.naver.OnATrip.entity.plan.QRoute.route;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long>, RouteRepositoryCustom {

}
