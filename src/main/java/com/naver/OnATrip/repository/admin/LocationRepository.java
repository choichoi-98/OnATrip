package com.naver.OnATrip.repository.admin;

import com.naver.OnATrip.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    // 도시 중복검사
    boolean existsByCity(String cityName);

    // 국가 중복검사
    boolean existsByCountryName(String countryName);
}
