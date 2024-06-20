package com.naver.OnATrip.repository;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.pay.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Repository
public class PayRepository {

    private final EntityManager em;
    private final JPAQueryFactory query;

    public PayRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }






}
