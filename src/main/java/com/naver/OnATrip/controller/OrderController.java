package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.pay.Orders;
import com.naver.OnATrip.entity.pay.Pay;
import com.naver.OnATrip.service.OrderService;
import com.naver.OnATrip.web.dto.pay.OrderDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    private final OrderService orderService;
    private final HttpSession httpSession;

    @PostMapping("/save_buyerInfo") //결제 정보 저장
    @ResponseBody
    public void save_buyerInfo(@RequestBody Pay request) {
        log.info("Saving buyer information: {}", request);
        orderService.save_buyerInfo(request);
        log.info("Buyer information saved successfully");
    }

    @PostMapping("/save_orderInfo") //주문 정보 저장
    @ResponseBody
    public String orderDone(@RequestBody OrderDto request, Model model) {
        log.info("Saving order information: {}", request);

        Orders orders = Orders.builder()
                .merchantUid(request.getMerchantUid())
                .totalPrice(request.getTotalPrice())
                .build();

        orderService.save_orderInfo(orders);
        log.info("Order information saved successfully for merchantUid: {}", request.getMerchantUid());


        return request.getMerchantUid();
    }

//    @PostMapping("/create")
//    public ResponseEntity<String> createOrder(@RequestBody Map<String, Object> payload){
//           //* 임시로 itemId설정 *//
//         int itemId = 1;
//
////        List<Integer> itemIdsInteger = (List<Integer>) payload.get("itemIds");
////        List<Long> itemIds = itemIdsInteger.stream().map(Long::valueOf).collect(Collectors.toList());
//
//        //요청받은 상품ID로 주문 테이블 생성
//        Orders temporaryOrder = orderService.creatOrder(itemId);
//
//        //세션에 임시 주문 정보를 저장
//        httpSession.setAttribute("temporaryOrder", temporaryOrder);
//        httpSession.setAttribute("itemId", itemId);   //상품ID 저장
//
//        Object itemIdsAttr = httpSession.getAttribute("itemIds");
//
//        return ResponseEntity.ok("주문 임시 저장 완료");
//    }

//    //최종 주문 테이블 생성
//    @PostMapping("/done")
//        public ResponseEntity<Object> completeOrder(@RequestBody OrderDto request){
//
//            Orders temporaryOrder = (Orders) httpSession.getAttribute("temporaryOrder");
//
//            if (temporaryOrder == null) {
//                return ResponseEntity.badRequest().body("임시 주문 정보를 찾을 수 없습니다.");
//            }
//
//            // 주문을 최종 확정하고 처리하는 서비스 메서드 호출
//            Orders completedOrder = orderService.orderConfirm(temporaryOrder);
//
//            // 주문 결과를 담은 응답 DTO 생성
//            OrderResponseDto orderResponseDto = new OrderResponseDto(completedOrder);
//
//            // 주문 완료 후 세션에서 임시 주문 정보 제거 (선택 사항)
//            httpSession.removeAttribute("temporaryOrder");
//            httpSession.removeAttribute("itemId");
//
//            // Pay로 리디렉션하거나 응답 DTO를 반환하여 사용자에게 결과 표시
//            return ResponseEntity.ok(orderResponseDto);
//    }

}
