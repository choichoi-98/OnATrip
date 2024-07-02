package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.pay.Item;
import com.naver.OnATrip.entity.pay.Orders;
import com.naver.OnATrip.repository.pay.ItemRepositoryCustom;
import com.naver.OnATrip.repository.pay.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepositoryCustom itemRepository;

//    @Transactional
//    public Orders creatOrder(int itemId) {
//        // 현재 인증된 사용자의 정보 가져오기
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//        // 사용자 정보를 기반으로 Member 객체 찾기
////        Member member = memberRepository.findByUsername(userDetails.getUsername())
////                .orElseThrow(() -> new NoSuchElementException("로그인 사용자를 찾을 수 없습니다."));
//
//        /*
//        * Security 적용 전 확인
//        * */
//        Member member = new Member();
//        member.setId(1L);
//        member.setEmail("test@example.com");
//        member.setName("tester");
//        member.setPassword("password123");
//
//        // 상품들 조회
//        List<Item> items = itemRepository.findAllById(itemId);
//
//        // 주문 생성
//        Orders order = new Orders();
//        order.setMember(member); // 현재 로그인한 사용자를 주문의 회원으로 설정
//        order.setItems(items); // 주문된 상품들 설정
//
//        // 주문서 저장
//        Orders savedOrder = orderRepository.save(order);
//
//        return savedOrder;
//    }

//    public Orders orderConfirm(Orders temporaryOrder) {
//        String merchantUid = generateMerchantUid(); // 주문번호 생성
//
//        // 사용자 입력 정보를 세션의 임시 주문 정보에 반영
//        temporaryOrder.setMerchantUid(merchantUid); // 주문번호 설정
//        temporaryOrder.setPaymentMethod(orderDto.getPaymentMethod()); // 결제 방법 설정 (예: 카드, 계좌이체 등)
//
//        // 추가적으로 필요한 정보가 있다면 temporaryOrder에 설정
//
//        // 주문 확정 후 저장
//        Orders completedOrder = orderRepository.save(temporaryOrder);
//
//        return completedOrder;
//    }
}
