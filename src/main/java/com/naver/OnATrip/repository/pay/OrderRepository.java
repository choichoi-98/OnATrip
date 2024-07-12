package com.naver.OnATrip.repository.pay;

import com.naver.OnATrip.entity.pay.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Query("select o from Orders o " +
            "left join fetch o.payments p " +
            "left join fetch o.member m " +
            "where o.orderUid = :orderUid ")
    Optional<Orders> findOrderAndPaymentAndMember(String orderUid);

    @Query("select o from Orders o" +
            " left join fetch o.payments p" +
            " where o.orderUid = :orderUid")
    Optional<Orders> findOrderAndPayment(String orderUid);


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
