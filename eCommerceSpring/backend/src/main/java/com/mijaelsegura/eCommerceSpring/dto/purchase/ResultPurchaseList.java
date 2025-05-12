package com.mijaelsegura.eCommerceSpring.dto.purchase;

import com.mijaelsegura.eCommerceSpring.dto.Result;

import java.util.List;

public class ResultPurchaseList extends Result {
    private List<PurchaseDto> purchases;

    public ResultPurchaseList() {

    }

    public List<PurchaseDto> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<PurchaseDto> purchases) {
        this.purchases = purchases;
    }
}
