package com.naver.OnATrip.repository;


import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.VerifyCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

//    Member findByEmailOne(String email);
    boolean existsByEmail(String email);

    Optional<Member> findByEmail(String email);

    void deleteByEmail(String email);

    Page<Member> findAll(Pageable pageable);


}
