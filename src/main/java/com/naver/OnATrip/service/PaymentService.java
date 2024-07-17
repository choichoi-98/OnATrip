package com.naver.OnATrip.service;

//import com.naver.OnATrip.entity.pay.PrePaymentEntity;
import com.naver.OnATrip.entity.pay.PrePaymentEntity;
import com.naver.OnATrip.repository.pay.PayRepository;
import com.naver.OnATrip.repository.pay.PrePaymentRepository;
import com.naver.OnATrip.web.dto.pay.PaymentDto;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@Slf4j
public class PaymentService {

    private final IamportClient iamportClient;

    @Autowired
    private PayRepository payRepository;

    @Autowired
    private PrePaymentRepository prePaymentRepository;


    @Value("${imp.api.key}")    //application.yml에서 값을 불러와서 사용
    private String impKey;

    @Value("${imp.api.secretKey}")
    private String impSecret;

    public PaymentService(){
        this.iamportClient = new IamportClient(impKey, impSecret);
    }

    @Transactional
    public void postPrepare(PrePaymentEntity request) throws IamportResponseException, IOException {

        PrepareData prepareData = new PrepareData(request.getMerchantUid(), request.getAmount());

        try {
            iamportClient.postPrepare(prepareData);  // 사전 등록 API
        } catch (IamportResponseException | IOException e){
//            throw  e;
            e.printStackTrace();
        }

//        iamportClient.postPrepare(prepareData);
        prePaymentRepository.save(request); // 주문번호와 결제예정 금액 DB 저장

    }


    @PostConstruct
    public void init(){
        System.out.println("impKey = " + impKey);
        System.out.println("impSecret = " + impSecret);
    }

//    @Transactional
//    public Orders insertPay(OrderDto request /*, Long memberId */ ) {
//        log.info("insertPay()");
////        Member member = MemberRepository.findById(memberId)
////                .orElseThrow(() -> new IllegalArgumentException("Invalid member Id:" + memberId));
//
////        Orders order = new Orders(request, member);
//        Orders order = new Orders(request);
//        return payRepository.save(order);
//    }



    public Payment validatePayment(PaymentDto request) throws IamportResponseException, IOException {
        PrePaymentEntity prePayment = prePaymentRepository.findByMerchantUid(request.getMerchantUid()).orElseThrow();
        BigDecimal preAmount = prePayment.getAmount();  //DB에 저장된 결제 요청 금액

        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getImpUid());
        BigDecimal paidAmount = iamportResponse.getResponse().getAmount(); //사용자가 실제 결제한 금액

        if (!preAmount.equals(paidAmount)){
            CancelData cancelData = cancelPayment(iamportResponse);
            iamportClient.cancelPaymentByImpUid(cancelData);
        }
        return iamportResponse.getResponse();
    }

//    public Payment validatePayment(PayInfoDto request) throws IamportResponseException, IOException {
//        PrePaymentEntity prePayment = prePaymentRepository.findByMerchantUid(request.getMerchant_uid()).orElseThrow();
//        BigDecimal preAmount = prePayment.getAmount();  //DB에 저장된 결제 요청 금액
//
//        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getImp_uid());
//        BigDecimal paidAmount = iamportResponse.getResponse().getAmount(); //사용자가 실제 결제한 금액
//
//        if (!preAmount.equals(paidAmount)){
//            CancelData cancelData = cancelPayment(iamportResponse);
//            iamportClient.cancelPaymentByImpUid(cancelData);
//        }
//        return iamportResponse.getResponse();
//    }

    @Transactional
    public CancelData cancelPayment(IamportResponse<Payment> response) {
        return new CancelData(response.getResponse().getImpUid(), true);
    }


//    public ModelAndView paymentContents(String merchantUid, HttpSession session, ModelAndView mv) {
//        log.info("paymentContents()");
//        mv = new ModelAndView();
//
//        //주문정보 가져오기
//        PayInfoDto payInfoDto = payRepository.findByMerchant_uid();
//
//    }
}
