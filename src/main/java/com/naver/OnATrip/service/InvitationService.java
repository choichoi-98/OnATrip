package com.naver.OnATrip.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationService {

    private final AlertService alertService;

    public void sendInvitation(String email, Long planId) {
        String sourceMemberEmail = alertService.getLoggedInUserEmail(); // 로그인된 사용자의 이메일 가져오기
        String message = "ON A Trip! 여행일정을 함께 하시겠습니까?";

        try {
            alertService.sendFriendInviteAlert(email, sourceMemberEmail, planId, message);
        } catch (IllegalStateException e) {
            // 중복 초대 예외 처리
            throw new IllegalStateException("이미 초대된 친구입니다.");
        } catch (Exception e) {
            // 기타 예외 처리
            throw new RuntimeException("초대장 보내기 중 오류 발생", e);
        }
    }
}