package com.sparta.blackwhitedeliverydriver.controller;

import com.sparta.blackwhitedeliverydriver.dto.PayApproveResponseDto;
import com.sparta.blackwhitedeliverydriver.dto.PayRequestDto;
import com.sparta.blackwhitedeliverydriver.dto.PayReadyResponseDto;
import com.sparta.blackwhitedeliverydriver.security.UserDetailsImpl;
import com.sparta.blackwhitedeliverydriver.service.PayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/pay")
public class PayController {

    private final PayService payService;

    @Secured({"ROLE_CUSTOMER"})
    @PostMapping("/ready")
    public ResponseEntity<PayReadyResponseDto> readyToPay(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @RequestBody PayRequestDto request) {
        //결제 준비
        PayReadyResponseDto response = payService.readyToPay(userDetails.getUsername(), request);
        //201 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Secured({"ROLE_CUSTOMER"})
    @GetMapping("/success")
    public ResponseEntity<PayApproveResponseDto> afterPay(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                          @RequestParam("pg_token") String pgToken,
                                                          @RequestParam("tid") String tid) {
        log.info("{}", tid);
        PayApproveResponseDto response = payService.approvePay(userDetails.getUsername(), pgToken, tid);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
