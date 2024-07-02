package com.naver.OnATrip.repository.pay;

import com.naver.OnATrip.entity.pay.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, ItemRepositoryCustom, QuerydslPredicateExecutor<Item> {

    List<Item> findAllById(int id);
}
