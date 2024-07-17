package com.naver.OnATrip.repository.pay;

import com.naver.OnATrip.entity.pay.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepository extends JpaRepository<Payment, Long> {

    void findByMerchantUid(String merchantUid);
}
