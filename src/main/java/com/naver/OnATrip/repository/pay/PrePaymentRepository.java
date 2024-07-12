package com.naver.OnATrip.repository.pay;

import com.naver.OnATrip.entity.pay.PrePaymentEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrePaymentRepository extends JpaRepository<PrePaymentEntity, Long> {

    Optional<PrePaymentEntity> findByMerchantUid(String merchantUid);
}
