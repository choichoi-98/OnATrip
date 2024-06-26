package com.naver.OnATrip.service;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


}
