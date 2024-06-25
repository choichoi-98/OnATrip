package com.naver.OnATrip.service;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)//데이터 변경하는 것은 트랜잭션 안에서 일어나야 한다.
//@RequiredArgsConstructor         //final 있는 필드만 가지고 생성자를 만들어준다.
public class PaymentService {

    private IamportClient api;

    public PaymentService(){
        this.api = new IamportClient("5567300760752543", "akexds3WZwtp2HRTUZtWHI7Nk1SFMg4ZT6IQtTKWM7O8xOaeZqDZsWyHwaEnqj7qwAf1TzyadBK4ouUv");
    }



}
