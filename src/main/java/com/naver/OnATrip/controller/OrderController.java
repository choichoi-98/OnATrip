package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.pay.*;
import com.naver.OnATrip.repository.MemberRepository;
import com.naver.OnATrip.service.MemberService;
import com.naver.OnATrip.service.OrderService;
import com.naver.OnATrip.service.PaymentService;
import com.naver.OnATrip.web.dto.pay.OrderDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;
    private final HttpSession httpSession;
    private final MemberService memberService;

    public OrderController(OrderService orderService, PaymentService paymentService, HttpSession httpSession, MemberService memberService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.httpSession = httpSession;
        this.memberService = memberService;
    }

    @PostMapping("/payment/save_buyerInfo") //결제 정보 저장
    @ResponseBody
    public void save_buyerInfo(@RequestBody Payment request) {
        System.out.println("Saving buyer information: {}");
        orderService.save_buyerInfo(request);
        System.out.println("Buyer information saved successfully");
//        log.info("Buyer information saved successfully");
    }

    @PostMapping("/payment/save_orderInfo") //주문 정보 저장
    @ResponseBody
    public String orderDone(@RequestBody OrderDto request, Model model, Principal principal) {
        System.out.println("Saving order information: {}");

        String memberId = principal.getName();

        Orders orders = Orders.builder()
                .merchantUid(request.getMerchantUid())
                .amount(request.getAmount())
                .payMethod(request.getPayMethod())
                .itemId(request.getItemId())
                .itemPeriod((int) request.getItemPeriod())
                .memberId(principal.getName())
                .paymentStatus(true)
                .build();

        orderService.save_orderInfo(orders);
        System.out.println("Order information saved successfully for merchantUid: {}");


        LocalDate currentDate = LocalDate.now();
        LocalDate endDate = currentDate.plusDays(request.getItemPeriod());

        Subscribe subscribe = Subscribe.builder()
                .memberId(principal.getName())
                .itemPeriod(request.getItemPeriod())
                .endDate(String.valueOf(endDate))
                .status(SubscribeStatus.ON)
                .build();

        orderService.save_subscribe(subscribe);

        Member member = memberService.findByEmail(memberId);

        member.setSubscribe_status("ON");
        memberService.save(member);

        System.out.println("*** subscribe DB 저장 ***");

        return request.getMerchantUid();
    }


    @GetMapping("/check-subscribe")
    public ResponseEntity<Map<String, Object>> checkAuthenticationStatus(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        if (authentication == null || !authentication.isAuthenticated()) {
            response.put("isAuthenticated", false);
            return ResponseEntity.ok(response);
        }

        // 인증된 사용자라면, 구독 상태를 가져옵니다.
        User user = (User) authentication.getPrincipal();
        Member member = memberService.findByEmail(user.getUsername());

        response.put("isAuthenticated", true);
        response.put("subscribeStatus", member.getSubscribe_status()); // 구독 상태 추가
        return ResponseEntity.ok(response);
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
