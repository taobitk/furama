// File: src/main/java/model/Contract.java
package model;

import java.sql.Timestamp; // <<<=== THÊM IMPORT NÀY
import java.time.LocalDateTime;

public class Contract {
    private int contractId;
    private LocalDateTime contractStartDate; // Giữ nguyên LocalDateTime
    private LocalDateTime contractEndDate;   // Giữ nguyên LocalDateTime
    private double contractDeposit;
    private double contractTotalMoney;
    private int employeeId;
    private int customerId;
    private int serviceId;

    public Contract() {
    }

    public Contract(int contractId, LocalDateTime contractStartDate, LocalDateTime contractEndDate,
                    double contractDeposit, double contractTotalMoney, int employeeId,
                    int customerId, int serviceId) {
        this.contractId = contractId;
        this.contractStartDate = contractStartDate;
        this.contractEndDate = contractEndDate;
        this.contractDeposit = contractDeposit;
        this.contractTotalMoney = contractTotalMoney;
        this.employeeId = employeeId;
        this.customerId = customerId;
        this.serviceId = serviceId;
    }

    // --- Getters and Setters (Giữ nguyên các getter/setter cũ) ---
    public int getContractId() { return contractId; }
    public void setContractId(int contractId) { this.contractId = contractId; }
    public LocalDateTime getContractStartDate() { return contractStartDate; } // Giữ getter cũ
    public void setContractStartDate(LocalDateTime contractStartDate) { this.contractStartDate = contractStartDate; } // Giữ setter cũ
    public LocalDateTime getContractEndDate() { return contractEndDate; } // Giữ getter cũ
    public void setContractEndDate(LocalDateTime contractEndDate) { this.contractEndDate = contractEndDate; } // Giữ setter cũ
    public double getContractDeposit() { return contractDeposit; }
    public void setContractDeposit(double contractDeposit) { this.contractDeposit = contractDeposit; }
    public double getContractTotalMoney() { return contractTotalMoney; }
    public void setContractTotalMoney(double contractTotalMoney) { this.contractTotalMoney = contractTotalMoney; }
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public Timestamp getStartDateTimestamp() {
        if (this.contractStartDate == null) {
            return null;
        }
        return Timestamp.valueOf(this.contractStartDate); // Chuyển đổi LocalDateTime -> Timestamp
    }

    public Timestamp getEndDateTimestamp() {
        if (this.contractEndDate == null) {
            return null;
        }
        return Timestamp.valueOf(this.contractEndDate); // Chuyển đổi LocalDateTime -> Timestamp
    }

}
