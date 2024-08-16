package com.naver.OnATrip.controller;

import com.google.gson.JsonIOException;
import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.pay.Item;
import com.naver.OnATrip.entity.pay.PrePaymentEntity;
import com.naver.OnATrip.service.ItemService;
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

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PayController {


    private final PaymentService paymentService;
    private final ItemService itemService;


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

    @PostMapping("/payPage")
    public String getItemById(@RequestParam("item_id") int itemId, Model model) {
        Optional<Item> itemOptional = itemService.findById(itemId);
        if (itemOptional.isPresent()) {
            Item item = itemOptional.get();
            model.addAttribute("item", item);
        }
        return "pay/payPage";
    }

    @PostMapping("/payment/validate")
    @ResponseBody
    public Payment validatePayment(@RequestBody PaymentDto request)
            throws IamportResponseException, IOException, JsonIOException {
//        log.info("Validating payment: {}", request);
        System.out.println("Validating payment");
        return paymentService.validatePayment(request);
    }


    @GetMapping("/myPage/subscribe")
    public String subscribe(Model model) {

        return "pay/myPage_Subscribe";
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
