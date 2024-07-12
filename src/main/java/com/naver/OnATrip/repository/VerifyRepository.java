package com.naver.OnATrip.repository;

import com.naver.OnATrip.entity.VerifyCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface VerifyRepository extends JpaRepository<VerifyCode, Long> {
    public VerifyCode findByEmail(String email);
    public VerifyCode findByCode(String code);
    public boolean existsByEmail(String email);
    @Transactional
    public void deleteByEmail(String email);

}
