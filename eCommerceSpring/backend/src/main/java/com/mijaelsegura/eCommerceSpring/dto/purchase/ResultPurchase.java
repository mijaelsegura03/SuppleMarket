package com.mijaelsegura.eCommerceSpring.dto.purchase;

import com.mijaelsegura.eCommerceSpring.dto.Result;

public class ResultPurchase extends Result {

    private PurchaseDto purchase;
    public ResultPurchase(String message, boolean success, String typeError, PurchaseDto purchaseDto) {
        super(message, success, typeError);
        purchase = purchaseDto;
    }

    public ResultPurchase() {

    }
    public PurchaseDto getPurchase() {
        return purchase;
    }

    public void setPurchase(PurchaseDto purchase) {
        this.purchase = purchase;
    }

}
