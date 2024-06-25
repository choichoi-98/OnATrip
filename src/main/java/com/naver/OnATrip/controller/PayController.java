package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.pay.Pay;
import com.naver.OnATrip.service.PaymentService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class PayController {

    private final PaymentService paymentService;

    @GetMapping("/subscribe")
    public String subscribe() {

        return "pay/subscribe";
    }

    @GetMapping("/payPage")
    public String pay() {

        return "pay/payPage";
    }

    //사전검증
//    @PostMapping("/payment/prepare")
//    public void preparePayment(@RequestBody Pay request)
//            throws IamportResponseException, IOException{
//        paymentService.postPrepare(request);
//
//    }

//    //결제요청
//    @PostMapping("/ready")
//    public ResponseEntity readyToPay(){
//        return payService.payReady();
//    }
//
//    //결제 진행 중 취소
//    @GetMapping("/cancel")
//    public void cancel(){
//        throw new BusinessLogicException(ExceptionCode.PAY_CANCEL);
//    }
//
//    //결제 실패
//    @GetMapping("/fail")
//    public void fail(){
//        throw new BusinessLogicException(ExceptionCode.PAY_FAILED);
//    }


}
