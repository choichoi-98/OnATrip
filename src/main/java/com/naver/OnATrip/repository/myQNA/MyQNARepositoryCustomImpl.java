package com.naver.OnATrip.repository.myQNA;

import com.naver.OnATrip.entity.MyQNA;
import com.naver.OnATrip.repository.plan.PlanRepositoryCustomImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Transient;

import java.util.List;

import static com.naver.OnATrip.entity.QMyQNA.myQNA;


public class MyQNARepositoryCustomImpl implements MyQNARepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public MyQNARepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
    private static final Logger logger = LoggerFactory.getLogger(PlanRepositoryCustomImpl.class);


    @Override
    @Transient
    public List<MyQNA> findAll(String email) {
        return  queryFactory
                .select(myQNA)
                .from(myQNA)
                .where(myQNA.member.email.eq(email))
                .fetch();
    }

}
