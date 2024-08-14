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
import java.util.Optional;

@Repository
public interface SubscribeRepository extends JpaRepository<Subscribe, Long> {

    @Query("select s from Subscribe s where s.memberId = :memberId and s.status = :status")
    Optional<Subscribe> findByMemberId(@Param("memberId")String memberId, @Param("status") SubscribeStatus status);

}