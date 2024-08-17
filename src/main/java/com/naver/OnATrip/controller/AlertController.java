package com.naver.OnATrip.controller;

import com.naver.OnATrip.entity.Alert;
import com.naver.OnATrip.service.AlertService;
import com.naver.OnATrip.service.InvitationService;
import com.naver.OnATrip.web.dto.AlertDto;
import com.naver.OnATrip.web.dto.InvitationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertController {


    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);
    private final InvitationService invitationService;
    private final AlertService alertService;

    @PostMapping("/sendInvitation")
    public ResponseEntity<String> sendInvitation(@RequestBody @Valid InvitationRequest request) {
        System.out.println("============================alertControler===========");
        try {
            System.out.println("request.getEmail() + request.getPlanId() = " + request.getEmail() + request.getPlanId());
            invitationService.sendInvitation(request.getEmail(), request.getPlanId());
            return ResponseEntity.ok("Invitation sent successfully.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 초대된 친구입니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request parameters.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while sending the invitation.");
        }
    }


    @GetMapping("/myAlerts")
    public ResponseEntity<List<AlertDto>> getMyAlerts(Principal principal) {
        String email = principal.getName();

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 알림 목록 가져오기
        List<Alert> alerts = alertService.getAlertsForUser(email);
        List<AlertDto> alertDtos = alerts.stream().map(AlertDto::fromEntity).collect(Collectors.toList());
        

        return ResponseEntity.ok(alertDtos);
    }
/*
    //알람 수락
    @PostMapping("/acceptAlert/{id}")
    public ResponseEntity<String> acceptAlert(@PathVariable Long id) {
        try {
            alertService.acceptAlert(id); // 알림 수락 처리 로직
            return ResponseEntity.ok("Alert accepted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while accepting the alert.");
        }
    }

    //알람 거절
    @PostMapping("/rejectAlert/{id}")
    public ResponseEntity<String> rejectAlert(@PathVariable Long id) {
        try {
            alertService.rejectAlert(id); // 알림 거절 처리 로직
            return ResponseEntity.ok("Alert rejected successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while rejecting the alert.");
        }
    }*/
}