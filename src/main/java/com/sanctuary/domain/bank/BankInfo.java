package com.sanctuary.domain.bank;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BankInfo {
    public static String BANK_NAME;
    public static String TAGLINE = "Where Balance Meets Trust";
    public static String BANK_CODE;
    public static String SWIFT_CODE;
    public static String IFSC_PREFIX = "SOEB";
    public static String RBI_LICENSE;
    public static String HEAD_OFFICE = "Mumbai, Maharashtra, India";
    public static String WEBSITE = "www.sanctuaryofequilibrium.com";
    public static String SUPPORT_EMAIL = "support@sanctuaryofequilibrium.com";
    public static String SUPPORT_PHONE = "1800-SOE-BANK";

    @Value("${app.bank.name}")
    public void setBankName(String name) { BANK_NAME = name; }

    @Value("${app.bank.code}")
    public void setBankCode(String code) { BANK_CODE = code; }

    @Value("${app.bank.swift}")
    public void setSwiftCode(String swift) { SWIFT_CODE = swift; }

    @Value("${app.bank.rbi-license}")
    public void setRbiLicense(String license) { RBI_LICENSE = license; }
}
