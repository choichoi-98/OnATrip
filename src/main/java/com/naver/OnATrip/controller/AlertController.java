package com.naver.OnATrip.controller;

import com.naver.OnATrip.service.InvitationService;
import com.naver.OnATrip.web.dto.InvitationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final InvitationService invitationService;

    @PostMapping("/sendInvitation")
    public ResponseEntity<String> sendInvitation(@RequestBody @Valid InvitationRequest request) {
        try {
            invitationService.sendInvitation(request.getEmail(), request.getPlanId());
            return ResponseEntity.ok("Invitation sent successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request parameters.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while sending the invitation.");
        }
    }

}