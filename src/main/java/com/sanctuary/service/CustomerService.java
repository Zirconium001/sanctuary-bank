package com.sanctuary.service;

import com.sanctuary.domain.customer.*;
import com.sanctuary.exception.*;
import com.sanctuary.repository.AccountRepository;
import com.sanctuary.repository.CustomerRepository;
import com.sanctuary.util.InputSanitizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository customerRepo;
    private final AccountRepository accountRepo;

    public CustomerService(CustomerRepository customerRepo, AccountRepository accountRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
    }

    public Customer registerCustomer(String name, String dob, String phone, String email, String password) throws BankingException {
        String cleanName = InputSanitizer.sanitizeName(name);
        String cleanPhone = InputSanitizer.sanitizePhone(phone);
        String cleanEmail = InputSanitizer.sanitizeEmail(email);

        if (customerRepo.findByEmail(cleanEmail).isPresent()) {
            throw new InvalidAmountException("A customer with this email already exists.");
        }

        Customer customer = new Customer(cleanName, dob, cleanPhone, cleanEmail);
        customer.setPassword(password);
        customer = customerRepo.save(customer);

        return customer;
    }

    public Customer findCustomer(String customerId) throws CustomerNotFoundException {
        return customerRepo.findById(customerId)
            .orElseThrow(() -> new CustomerNotFoundException(customerId));
    }

    public Customer findByEmail(String email) {
        return customerRepo.findByEmail(email).orElse(null);
    }

    public List<Customer> getAllCustomers() {
        return customerRepo.findAll();
    }

    public void updateKycStatus(String customerId, KycStatus status) throws CustomerNotFoundException {
        Customer c = findCustomer(customerId);
        c.setKycStatus(status);
        customerRepo.save(c);
    }

    public void setAddress(String customerId, String line1, String line2, String city, String state, String postalCode, String country) throws CustomerNotFoundException {
        Customer c = findCustomer(customerId);
        c.setAddress(line1, line2, city, state, postalCode, country);
        customerRepo.save(c);
    }

    public void evaluateAndUpgradeTier(String customerId) throws CustomerNotFoundException {
        Customer c = findCustomer(customerId);
        double totalBalance = accountRepo.findByCustomerId(customerId).stream()
            .mapToDouble(a -> a.getBalance())
            .sum();
        CustomerTier newTier = CustomerTier.evaluate(totalBalance);
        c.setTier(newTier);
        customerRepo.save(c);
    }
}
