package com.mijaelsegura.eCommerceSpring.services;

import com.mijaelsegura.eCommerceSpring.dto.purchase.PurchaseDto;
import com.mijaelsegura.eCommerceSpring.dto.purchase.ResultPurchase;
import com.mijaelsegura.eCommerceSpring.dto.purchase.ResultPurchaseList;

public interface IPurchaseService {
    ResultPurchase GetPurchaseById(long id);
    ResultPurchaseList GetAllPurchases();
    ResultPurchase PostPurchase(PurchaseDto purchaseDto);
    ResultPurchase DeletePurchase(long id);
}
