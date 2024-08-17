package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.pay.Subscribe;
import com.naver.OnATrip.entity.pay.SubscribeStatus;
import com.naver.OnATrip.repository.MemberRepository;
import com.naver.OnATrip.repository.pay.SubscribeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SubscribeService {

    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private MemberRepository memberRepository;

    public SubscribeService(SubscribeRepository subscribeRepository) {
        this.subscribeRepository = subscribeRepository;
    }


    @Scheduled(cron = "0 0 0 * * ?")    //매일밤 자정에 실행
    @Transactional
    public void updateSubscription(){
        List<Subscribe> subscribes = subscribeRepository.findAllByEndDateBefore(LocalDate.now());
        for (Subscribe subscribe : subscribes) {
            //구독권 - 구독 상태 OFF로 변경
            subscribe.setStatus(SubscribeStatus.OFF);

            //해당 회원의 구독 상태도 OFF로 변경
            Member member = subscribe.getMember();
            if (member != null){
                member.setSubscribe_status(String.valueOf(SubscribeStatus.OFF));
            }
        }
        subscribeRepository.saveAll(subscribes);
    }


}
