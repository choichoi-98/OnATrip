package com.naver.OnATrip.repository.myQNA;

import com.naver.OnATrip.entity.MyQNA;

import java.util.List;


public interface MyQNARepositoryCustom {

    List<MyQNA> findAll(String email);
}
