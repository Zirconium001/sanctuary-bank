package com.sanctuary.util;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;

public class IdGenerator {
    private static final String COUNTER_FILE = "data/system/id_counter.properties";
    private static final Properties counters = new Properties();
    private static boolean loaded = false;

    private static void ensureLoaded() {
        if (loaded) return;
        try {
            File f = new File(COUNTER_FILE);
            if (f.exists()) {
                try (InputStream in = new FileInputStream(f)) {
                    counters.load(in);
                }
            }
            loaded = true;
        } catch (IOException e) {
            loaded = true;
        }
    }

    private static synchronized int nextVal(String key) {
        ensureLoaded();
        int val = 1;
        String s = counters.getProperty(key);
        if (s != null) {
            try { val = Integer.parseInt(s) + 1; } catch (NumberFormatException e) { val = 1; }
        }
        counters.setProperty(key, String.valueOf(val));
        try {
            File f = new File(COUNTER_FILE);
            f.getParentFile().mkdirs();
            try (OutputStream out = new FileOutputStream(f)) {
                counters.store(out, "Sanctuary of Equilibrium - ID Counters");
            }
        } catch (IOException e) {
            System.err.println("Warning: could not persist ID counter: " + e.getMessage());
        }
        return val;
    }

    public static String nextCustomerId() {
        return "C-" + String.format("%05d", nextVal("customer"));
    }

    public static String nextAccountNumber() {
        return "SOE-" + String.format("%08d", nextVal("account"));
    }

    public static String nextTransactionId() {
        return "T-" + String.format("%010d", nextVal("transaction"));
    }

    public static String nextEmployeeId() {
        return "E-" + String.format("%04d", nextVal("employee"));
    }

    public static String nextBranchCode() {
        return "BR-" + String.format("%03d", nextVal("branch"));
    }

    public static String nextLoanId() {
        return "L-" + String.format("%08d", nextVal("loan"));
    }
}
