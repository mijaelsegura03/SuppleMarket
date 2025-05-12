package com.mijaelsegura.eCommerceSpring.dto.purchase;

public class PurchaseDetailDto {
    private long purchaseId;
    private long supplementId;
    private int quantity;

    public long getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(long purchaseId) {
        this.purchaseId = purchaseId;
    }

    public long getSupplementId() {
        return supplementId;
    }

    public void setSupplementId(long supplementId) {
        this.supplementId = supplementId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
