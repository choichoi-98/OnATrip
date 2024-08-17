package com.naver.OnATrip.repository.pay;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.pay.*;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    @Query("select s from Subscribe s where s.member.email = :email and s.status = :status")
    Optional<Subscribe> findByMemberId(@Param("email")String email, @Param("status") SubscribeStatus status);

    @Query("select s from Subscribe s where s.endDate < :date")
    List<Subscribe> findAllByEndDateBefore(@Param("date")LocalDate date);
}