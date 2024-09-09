package com.naver.OnATrip.controller;

import com.google.gson.JsonIOException;
import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.pay.Item;
import com.naver.OnATrip.entity.pay.PrePaymentEntity;
import com.naver.OnATrip.entity.pay.Subscribe;
import com.naver.OnATrip.service.ItemService;
import com.naver.OnATrip.service.MemberService;
import com.naver.OnATrip.service.OrderService;
import com.naver.OnATrip.service.PaymentService;
import com.naver.OnATrip.web.dto.pay.PaymentDto;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PayController {

    private final PaymentService paymentService;
    private final ItemService itemService;
    private final MemberService memberService;
    private final OrderService orderService;

    /**
     * 아임포트 결제 검증 컨트롤러
     **/
    private IamportClient iamportClient;

    @Value("${imp.api.key}")    //application.yml에서 값을 불러와서 사용
    private String apiKey;

    @Value("${imp.api.secretKey}")
    private String secretKey;

    @PostConstruct
    private void init() {
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    //사전 검증
    @PostMapping("/payment/prepare")
    @ResponseBody
    public void preparePayment(@RequestBody PrePaymentEntity request)
            throws IamportResponseException, IOException {
        System.out.println("Preparing payment: {}");
        paymentService.postPrepare(request);
        System.out.println("Payment prepared successfully for merchant_uid");
    }

    //결제 상세 페이지
    @PostMapping("/payPage")
    public String getItemById(@RequestParam("item_id") int itemId, Model model) {
        Optional<Item> itemOptional = itemService.findById(itemId);

        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();

            LocalDate currentDate = LocalDate.now();
            LocalDate endDate = currentDate.plusDays(item.getPeriod());

            model.addAttribute("item", item);
            model.addAttribute("currentDate", currentDate);
            model.addAttribute("endDate", endDate);
        }
        return "pay/payPage";
    }

    //결제 인증
    @PostMapping("/payment/validate")
    @ResponseBody
    public Payment validatePayment(@RequestBody PaymentDto request)
            throws IamportResponseException, IOException, JsonIOException {
//        log.info("Validating payment: {}", request);
        System.out.println("Validating payment");
        return paymentService.validatePayment(request);
    }

    //마이페이지 - 결제 더보기 상세 페이지
    @GetMapping("/myPage/subscribe")
    public ModelAndView subscribe(ModelAndView mv, Principal principal) {
        String email = principal.getName();

        Member member = memberService.findByEmail(email);
        mv.addObject("member", member);

        Optional<Subscribe> subscribeOptional = orderService.findByMemberId(email);
        Subscribe subscribe = subscribeOptional.orElse(null);
        mv.addObject("subscribe", subscribe);

        // 구독 정보가 존재할 경우 아이템 이름 조회
        if (subscribe != null) {
            int itemId = subscribe.getItemId(); // 구독 정보에서 itemId 가져오기
            Optional<Item> item = itemService.findById(itemId); // itemId로 Item 조회

            if (item.isPresent()) {
                Item itemlist = item.get(); // Optional에서 Item 추출
                mv.addObject("itemlist", itemlist); // 조회된 item의 이름을 ModelAndView에 추가
            } else {
                // item이 존재하지 않을 경우 처리
                mv.addObject("itemlist", "구독권 정보 없음");
            }
        }
        mv.setViewName("pay/myPage_Subscribe");

        return mv;
    }

//
//    @GetMapping("/orderDone")
//    public String orderDone() {
////        PaymentDto paymentDto = OrderService.orderDone(merchant_uid);
////        model.addAttribute("paymentDto", paymentDto);
//
//        return "pay/orderEnd";
//    }




//    @PostMapping("/pay/validate")
//    @ResponseBody
//    public Payment validatePayment(@RequestBody PayInfoDto request)
//                    throws IamportResponseException, IOException{
//        log.info("Validating payment: {}", request);
//        return payService.validatePayment(request);
//    }

//    @GetMapping("payment")
//    public ModelAndView paymentContents(String merchant_uid, HttpSession session, ModelAndView mv){
//        log.info("paymentContents()");
//        mv = payService.paymentContents(merchant_uid, session);
//        return mv;
//    }

//    @PostMapping("/pay/validate")
//    @ResponseBody
//    public Payment validatePayment(@RequestBody PayInfoDto request)
//                    throws IamportResponseException, IOException{
//        log.info("Validating payment: {}", request);
//        return payService.validatePayment(request);
//    }


    /** 프론트에서 받은 PG사 결괏값을 통해 아임포트 토큰 발행 **/
//    @PostMapping("/{imp_uid}")
//    public IamportResponse<Payment> paymentByImpUid(@PathVariable String imp_uid) throws IamportResponseException, IOException{
//        log.info("paymentByImpUid 진입");
//        return iamportClient.paymentByImpUid(imp_uid);
//    }


//    @PostMapping("/{imp_uid}")
//    public IamportResponse<Payment> paymentByImpUid(Model model, Locale locale,
//                                                    HttpSession httpSession, @PathVariable(value = "imp_uid") String imp_uid)
//            throws IamportResponseException, IOException {
//        return api.paymentByImpUid(imp_uid);
//    }


}
