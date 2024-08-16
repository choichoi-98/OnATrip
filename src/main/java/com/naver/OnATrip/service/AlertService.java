package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.Alert;
import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.entity.plan.Plan;
import com.naver.OnATrip.repository.AlertRepository;
import com.naver.OnATrip.repository.MemberRepository;
import com.naver.OnATrip.repository.plan.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertService {

    private final AlertRepository alertRepository;
    private final MemberRepository memberRepository;
    private final PlanRepository planRepository;

    // 중복 초대 여부 확인
    public boolean isInvitationAlreadySent(String email, Long planId) {
        return alertRepository.existsByMemberEmailAndPlanId(email, planId);
    }

    // 친구 초대 알림 전송
    public void sendFriendInviteAlert(String email, String sourceMemberEmail, Long planId, String message) {
        // 중복 초대 확인
        if (isInvitationAlreadySent(email, planId)) {
            throw new IllegalStateException("이미 초대된 친구입니다.");
        }

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Member not found"));

        Member sourceMember = memberRepository.findByEmail(sourceMemberEmail)
                .orElseThrow(() -> new IllegalArgumentException("Source Member not found"));

        Plan plan = planRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Plan not found"));

        Alert alert = new Alert(member.getEmail(), sourceMember.getEmail(), plan, message);
        alertRepository.save(alert);
    }

    // 현재 로그인한 사용자의 이메일 반환
    public String getLoggedInUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            throw new IllegalStateException("User not authenticated");
        }
    }
}