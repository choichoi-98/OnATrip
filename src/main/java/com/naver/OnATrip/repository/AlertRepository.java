package com.naver.OnATrip.repository;

import com.naver.OnATrip.entity.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    List<Alert> findByMemberId(Long memberId);
}