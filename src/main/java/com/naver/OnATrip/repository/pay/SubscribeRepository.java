package com.naver.OnATrip.repository.pay;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.pay.*;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {



}