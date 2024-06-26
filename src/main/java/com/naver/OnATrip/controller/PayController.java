package com.naver.OnATrip.controller;

import com.naver.OnATrip.service.PayService;
import com.siot.IamportRestClient.IamportClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PayController {

    private final PayService payService;

    /** 아임포트 결제 검증 컨트롤러 **/
    private IamportClient iamportClient;

    @Value("${imp.api.key}")    //application.yml에서 값을 불러와서 사용
    private String apiKey;

    @Value("${imp.api.secretKey")
    private String secretKey;

    @PostConstruct
    private void init(){
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }
    /** 프론트에서 받은 PG사 결괏값을 통해 아임포트 토큰 발행 **/
//    @PostMapping("/{imp_uid}")
//    public IamportResponse<Payment> paymentByImpUid(@PathVariable String imp_uid) throws IamportResponseException, IOException{
//        log.info("paymentByImpUid 진입");
//        return iamportClient.paymentByImpUid(imp_uid);
//    }



    @GetMapping("/subscribe")
    public String subscribe() {

        return "pay/subscribe";
    }

    /*
    @GetMapping("/payPage")
    public String payOrder(Model model, ItemDto itemDto) {
    model.addAttribute("itemDto", itemDto);


        return "pay/payPage";
    }
*/


}
