package com.naver.OnATrip.service;

import com.naver.OnATrip.entity.Alert;
import com.naver.OnATrip.entity.Member;
import com.naver.OnATrip.repository.AlertRepository;
import com.naver.OnATrip.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AlertService {

    private final AlertRepository alertRepository;
    private final MemberRepository memberRepository;
    private final JavaMailSender mailSender;

    public void sendFriendInviteAlert(Long memberId, Long sourceMemberId, String message) {
        // 대상 회원과 소스 회원 찾기
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));
        Member sourceMember = memberRepository.findById(sourceMemberId).orElseThrow(() -> new IllegalArgumentException("Invalid source member ID"));

        // 알림 생성 및 저장
        Alert alert = new Alert(member, sourceMember, message);
        alertRepository.save(alert);

        // 이메일 전송
        sendEmail(member.getEmail(), "Friend Invite Alert", message);
    }

    private void sendEmail(String to, String subject, String text) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // HTML 사용 가능

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

}
