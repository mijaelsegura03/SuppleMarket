package com.mijaelsegura.eCommerceSpring.dto.purchase;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PurchaseDto {
    private long id;
    private BigDecimal totalPrice;
    private LocalDate purchaseDate;
    private long userDNI;
    private List<PurchaseDetailDto> purchaseDetails;

    public PurchaseDto() {

    }

    public PurchaseDto(Long id, BigDecimal totalPrice, LocalDate purchaseDate, long userDNI) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
        this.userDNI = userDNI;
    }

    public PurchaseDto(long id, BigDecimal totalPrice, LocalDate purchaseDate, long userDNI, List<PurchaseDetailDto> purchaseDetails) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.purchaseDate = purchaseDate;
        this.userDNI = userDNI;
        this.purchaseDetails = purchaseDetails;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public long getUserDNI() {
        return userDNI;
    }

    public void setUserDNI(long userDNI) {
        this.userDNI = userDNI;
    }

    public List<PurchaseDetailDto> getPurchaseDetails() {
        return purchaseDetails;
    }

    public void setPurchaseDetails(List<PurchaseDetailDto> purchaseDetails) {
        this.purchaseDetails = purchaseDetails;
    }

    public String validateAllProperties() {
        if (totalPrice == null) {
            return "Purchase total price is required.";
        } else if (purchaseDate == null) {
            return "Purchase date is required";
        } else if (userDNI == 0) {
            return "Purchase user DNI is required.";
        } else if (purchaseDetails.isEmpty()) {
            return "Purchase details are required";
        }
        return "";
    }
}
