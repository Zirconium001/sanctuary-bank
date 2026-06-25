package com.sanctuary.domain.customer;

public enum CustomerTier {
    REGULAR("Regular", 0.0, 50_000, 100_000),
    SILVER("Silver", 50_000.0, 200_000, 500_000),
    GOLD("Gold", 200_000.0, 500_000, 2_000_000),
    PLATINUM("Platinum", 500_000.0, 2_000_000, 10_000_000);

    private final String displayName;
    private final double minBalance;
    private final double dailyWithdrawalLimit;
    private final double dailyTransferLimit;

    CustomerTier(String displayName, double minBalance, double dailyWithdrawalLimit, double dailyTransferLimit) {
        this.displayName = displayName;
        this.minBalance = minBalance;
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
        this.dailyTransferLimit = dailyTransferLimit;
    }

    public String displayName() { return displayName; }
    public double minBalance() { return minBalance; }
    public double dailyWithdrawalLimit() { return dailyWithdrawalLimit; }
    public double dailyTransferLimit() { return dailyTransferLimit; }

    public static CustomerTier fromString(String s) {
        for (CustomerTier t : values()) {
            if (t.name().equalsIgnoreCase(s) || t.displayName.equalsIgnoreCase(s)) return t;
        }
        return REGULAR;
    }

    public static CustomerTier evaluate(double totalRelationshipValue) {
        if (totalRelationshipValue >= PLATINUM.minBalance) return PLATINUM;
        if (totalRelationshipValue >= GOLD.minBalance) return GOLD;
        if (totalRelationshipValue >= SILVER.minBalance) return SILVER;
        return REGULAR;
    }
}
