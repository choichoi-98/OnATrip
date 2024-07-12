package com.naver.OnATrip.repository.pay;

import com.naver.OnATrip.entity.pay.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

//    private final EntityManager em;
//    private final JPAQueryFactory queryFactory;
//
//
//    public OrderRepository(EntityManager em) {
//        this.em = em;
//        this.queryFactory = new JPAQueryFactory(em);
//    }
//
//    @Transactional
//    public Orders save(Orders order) {
//        em.persist(order);
//        return order;
//    }
}
