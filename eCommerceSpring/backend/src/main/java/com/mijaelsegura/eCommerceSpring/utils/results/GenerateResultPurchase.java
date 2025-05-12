package com.mijaelsegura.eCommerceSpring.utils.results;

import com.mijaelsegura.eCommerceSpring.dto.purchase.PurchaseDto;
import com.mijaelsegura.eCommerceSpring.dto.purchase.ResultPurchase;
import org.springframework.stereotype.Component;

@Component
public class GenerateResultPurchase implements GenerateResult<PurchaseDto> {


    @Override
    public ResultPurchase getSuccessResult(PurchaseDto purchase) {
        return new ResultPurchase("", true, "", purchase);
    }
}
