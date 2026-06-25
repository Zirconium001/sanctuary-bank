package com.sanctuary.controller;

import com.sanctuary.domain.product.InterestRateCard;
import com.sanctuary.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/rates")
public class RateController {

    @GetMapping("/savings")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getSavingsRates() {
        return ResponseEntity.ok(ApiResponse.ok("Savings interest rates", InterestRateCard.SAVINGS_RATES));
    }

    @GetMapping("/fd")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getFdRates() {
        return ResponseEntity.ok(ApiResponse.ok("Fixed deposit rates", InterestRateCard.FD_RATES));
    }

    @GetMapping("/loans")
    public ResponseEntity<ApiResponse<Map<String, Double>>> getLoanRates() {
        return ResponseEntity.ok(ApiResponse.ok("Loan interest rates", InterestRateCard.LOAN_RATES));
    }
}
