package com.sanctuary.domain.bank;

import com.sanctuary.util.IdGenerator;
import com.sanctuary.util.DateUtils;
import jakarta.persistence.*;

@Entity
@Table(name = "branches")
public class Branch {

    @Id
    private String branchCode;
    private String branchName;
    private String address;
    private String city;
    private String state;
    private String phone;
    private String managerName;
    private String openingDate;
    private boolean active;

    public Branch(String branchName, String address, String city, String state, String phone) {
        this.branchCode = IdGenerator.nextBranchCode();
        this.branchName = branchName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.phone = phone;
        this.openingDate = DateUtils.today();
        this.active = true;
    }

    public Branch() {}

    public String getBranchCode() { return branchCode; }
    public String getBranchName() { return branchName; }
    public String getCity() { return city; }
    public String getPhone() { return phone; }

    public void setBranchCode(String branchCode) { this.branchCode = branchCode; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setManagerName(String managerName) { this.managerName = managerName; }
    public void setOpeningDate(String openingDate) { this.openingDate = openingDate; }
    public void setActive(boolean active) { this.active = active; }
}
