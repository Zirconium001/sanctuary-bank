package com.sanctuary.repository;

import com.sanctuary.domain.transaction.TransactionRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionRecord, String> {
    List<TransactionRecord> findByAccountNumberOrderByTimestampDesc(String accountNumber);
    List<TransactionRecord> findByAccountNumber(String accountNumber);
    List<TransactionRecord> findByTimestampStartingWith(String date);
    long countByAccountNumber(String accountNumber);
}
