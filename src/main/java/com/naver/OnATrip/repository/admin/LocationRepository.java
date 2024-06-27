package com.naver.OnATrip.repository.admin;

import com.naver.OnATrip.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public class LocationRepository {

    public interface LoactionRepository extends JpaRepository<Location, Long> {
    }

    //여행지 추가
    public void addLocation(Location location) {
    }
}