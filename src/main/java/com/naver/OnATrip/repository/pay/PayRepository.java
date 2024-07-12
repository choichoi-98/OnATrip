package com.naver.OnATrip.repository.pay;

import com.naver.OnATrip.entity.pay.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayRepository extends JpaRepository<Payments, Long> {

//    private final EntityManager em;
//    private final JPAQueryFactory query;
//
//    public PayRepository(EntityManager em) {
//        this.em = em;
//        this.query = new JPAQueryFactory(em);
//    }





}
