package com.naver.OnATrip.repository;

import com.naver.OnATrip.entity.MyQNA;
import com.naver.OnATrip.web.dto.myQNA.MyQNAListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyQNARepository extends JpaRepository<MyQNA, Long> {
}
