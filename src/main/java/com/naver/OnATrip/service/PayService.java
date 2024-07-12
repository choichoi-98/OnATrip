package com.naver.OnATrip.service;

//import com.naver.OnATrip.entity.pay.PrePaymentEntity;
import com.naver.OnATrip.entity.pay.Pay;
import com.naver.OnATrip.entity.pay.PrePaymentEntity;
import com.naver.OnATrip.repository.pay.PayRepository;
import com.naver.OnATrip.repository.pay.PrePaymentRepository;
import com.naver.OnATrip.web.dto.pay.PayInfoDto;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.request.PrepareData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
public class PayService {

    private final IamportClient iamportClient;

    @Autowired
    private PayRepository payRepository;

    @Autowired
    private PrePaymentRepository prePaymentRepository;


    @Value("${imp.api.key}")    //application.yml에서 값을 불러와서 사용
    private String apiKey;

    @Value("${imp.api.secretKey}")
    private String secretKey;

    public PayService(){
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }

    public IamportClient getIamportClient() {
        return iamportClient;
    }


    public void postPrepare(PrePaymentEntity request) throws IamportResponseException, IOException {

        PrepareData prepareData = new PrepareData(request.getMerchantUid(), request.getAmount());
        iamportClient.postPrepare(prepareData);  // 사전 등록 API

        prePaymentRepository.save(request); // 주문번호와 결제예정 금액 DB 저장

    }

    public Payment validatePayment(PayInfoDto request) throws IamportResponseException, IOException {
        PrePaymentEntity prePayment = prePaymentRepository.findByMerchantUid(request.getMerchant_uid()).orElseThrow();
        BigDecimal preAmount = prePayment.getAmount();  //DB에 저장된 결제 요청 금액

        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getImp_uid());
        BigDecimal paidAmount = iamportResponse.getResponse().getAmount(); //사용자가 실제 결제한 금액

        if (!preAmount.equals(paidAmount)){
            CancelData cancelData = cancelPayment(iamportResponse);
            iamportClient.cancelPaymentByImpUid(cancelData);
        }
        return iamportResponse.getResponse();
    }

    public CancelData cancelPayment(IamportResponse<Payment> response) {
        return new CancelData(response.getResponse().getImpUid(), true);
    }
}
