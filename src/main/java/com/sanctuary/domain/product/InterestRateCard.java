package com.sanctuary.domain.product;

import java.util.*;

public class InterestRateCard {

    public static final Map<String, Double> SAVINGS_RATES = new LinkedHashMap<>();
    public static final Map<String, Double> FD_RATES = new LinkedHashMap<>();
    public static final Map<String, Double> LOAN_RATES = new LinkedHashMap<>();

    static {
        SAVINGS_RATES.put("Regular (up to 1L)", 0.030);
        SAVINGS_RATES.put("Above 1L - 10L", 0.035);
        SAVINGS_RATES.put("Above 10L - 1Cr", 0.040);
        SAVINGS_RATES.put("Above 1Cr", 0.045);

        FD_RATES.put("7-14 days", 0.035);
        FD_RATES.put("15-30 days", 0.040);
        FD_RATES.put("31-90 days", 0.045);
        FD_RATES.put("91-180 days", 0.050);
        FD_RATES.put("181-364 days", 0.055);
        FD_RATES.put("1 year", 0.065);
        FD_RATES.put("2 years", 0.065);
        FD_RATES.put("3 years", 0.070);
        FD_RATES.put("5 years", 0.075);

        LOAN_RATES.put("Personal Loan", 0.105);
        LOAN_RATES.put("Home Loan", 0.085);
        LOAN_RATES.put("Auto Loan", 0.090);
        LOAN_RATES.put("Education Loan", 0.080);
        LOAN_RATES.put("Business Loan", 0.110);
        LOAN_RATES.put("Gold Loan", 0.075);
    }

    public static double getFdRate(int tenureMonths) {
        if (tenureMonths <= 0) return FD_RATES.get("7-14 days");
        if (tenureMonths <= 1) return FD_RATES.get("15-30 days");
        if (tenureMonths <= 3) return FD_RATES.get("31-90 days");
        if (tenureMonths <= 6) return FD_RATES.get("91-180 days");
        if (tenureMonths <= 12) return FD_RATES.get("181-364 days");
        if (tenureMonths <= 12) return FD_RATES.get("1 year");
        if (tenureMonths <= 24) return FD_RATES.get("2 years");
        if (tenureMonths <= 36) return FD_RATES.get("3 years");
        return FD_RATES.get("5 years");
    }

    public static double getLoanRate(String loanType) {
        return LOAN_RATES.getOrDefault(loanType, 0.105);
    }

    private InterestRateCard() {}
}
