package com.naver.OnATrip.service;

import com.naver.OnATrip.web.dto.pay.response.PayReadyResponseDto;
import com.siot.IamportRestClient.Iamport;
import com.siot.IamportRestClient.IamportClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional(readOnly = true)//데이터 변경하는 것은 트랜잭션 안에서 일어나야 한다.
//@RequiredArgsConstructor         //final 있는 필드만 가지고 생성자를 만들어준다.
public class PaymentService {

    private IamportClient api;

    public PaymentService(){
        this.api = new IamportClient("5567300760752543", "akexds3WZwtp2HRTUZtWHI7Nk1SFMg4ZT6IQtTKWM7O8xOaeZqDZsWyHwaEnqj7qwAf1TzyadBK4ouUv");
    }

//    @Autowired
//    private PrePaymentRepository prePaymentRepository;

   // private PayReadyResponseDto payReady;


}
