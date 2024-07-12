package com.naver.OnATrip.repository.admin;

import com.naver.OnATrip.entity.Location;
import com.naver.OnATrip.web.dto.location.LocationDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // 도시 중복검사
    boolean existsByCity(String cityName);

    // 국가 중복검사
    boolean existsByCountryName(String countryName);

    // 메인 tab 부분(여행지 목록 저장)
    List<Location> findByLocationType(String locationType);
}
