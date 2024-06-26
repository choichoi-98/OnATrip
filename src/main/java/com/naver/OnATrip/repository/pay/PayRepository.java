package com.naver.OnATrip.repository.pay;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class PayRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public PayRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }





}
