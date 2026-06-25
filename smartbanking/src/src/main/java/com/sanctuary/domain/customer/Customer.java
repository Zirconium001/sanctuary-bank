package com.sanctuary.domain.customer;

import com.sanctuary.util.DateUtils;
import com.sanctuary.util.IdGenerator;
import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    private String customerId;
    private String fullName;
    private String dateOfBirth;
    private String phone;
    private String email;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    @Enumerated(EnumType.STRING)
    private CustomerTier tier;

    @Enumerated(EnumType.STRING)
    private KycStatus kycStatus;

    private String dateOnboarded;
    private boolean active;
    private String password;

    public Customer(String fullName, String dateOfBirth, String phone, String email) {
        this.customerId = IdGenerator.nextCustomerId();
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.email = email;
        this.tier = CustomerTier.REGULAR;
        this.kycStatus = KycStatus.PENDING;
        this.dateOnboarded = DateUtils.today();
        this.active = true;
        this.country = "India";
    }

    public Customer() {}

    public String getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getDateOfBirth() { return dateOfBirth; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddressLine1() { return addressLine1; }
    public String getAddressLine2() { return addressLine2; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPostalCode() { return postalCode; }
    public String getCountry() { return country; }
    public CustomerTier getTier() { return tier; }
    public KycStatus getKycStatus() { return kycStatus; }
    public String getDateOnboarded() { return dateOnboarded; }
    public boolean isActive() { return active; }
    public String getPassword() { return password; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    public void setAddress(String line1, String line2, String city, String state, String postalCode, String country) {
        this.addressLine1 = line1;
        this.addressLine2 = line2;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    public void setTier(CustomerTier tier) { this.tier = tier; }
    public void setKycStatus(KycStatus kycStatus) { this.kycStatus = kycStatus; }
    public void setActive(boolean active) { this.active = active; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public void setAddressLine1(String addressLine1) { this.addressLine1 = addressLine1; }
    public void setAddressLine2(String addressLine2) { this.addressLine2 = addressLine2; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setCountry(String country) { this.country = country; }
    public void setDateOnboarded(String dateOnboarded) { this.dateOnboarded = dateOnboarded; }
}
