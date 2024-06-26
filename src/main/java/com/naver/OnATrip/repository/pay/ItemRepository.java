package com.naver.OnATrip.repository.pay;

import com.naver.OnATrip.entity.pay.Item;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepository {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    public ItemRepository(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    public List<Item> findAllById(int itemId) {
        return em.createQuery("select i from Item i where i.id = :itemId", Item.class)
                .setParameter("itemId", itemId)
                .getResultList();
    }

}
