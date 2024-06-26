package com.naver.OnATrip.repository.pay;

import com.naver.OnATrip.entity.pay.Orders;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class OrderRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;


    public OrderRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Transactional
    public Orders save(Orders order) {
        em.persist(order);
        return order;
    }
}
