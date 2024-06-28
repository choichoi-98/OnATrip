package com.naver.OnATrip.service;

//import com.naver.OnATrip.entity.pay.PrePaymentEntity;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.PrepareData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
public class PayService {
    private final IamportClient iamportClient;

    @Value("${imp.api.key}")    //application.yml에서 값을 불러와서 사용
    private String apiKey;

    @Value("${imp.api.secretKey")
    private String secretKey;

    public PayService(){
        this.iamportClient = new IamportClient(apiKey, secretKey);
    }
    public IamportClient getIamportClient() {
        return iamportClient;
    }

//    public void postPrepare(PrePaymentEntity request) throws IamportResponseException, IOException {
//        PrepareData prepareData = new PrepareData(request.getMerchantUid(), request.getAmount());
//        iamportClient.postPrepare(prepareData);  // 사전 등록 API
//
//        //prePaymentRepository.save(request); // 주문번호와 결제예정 금액 DB 저장
//    }
}
