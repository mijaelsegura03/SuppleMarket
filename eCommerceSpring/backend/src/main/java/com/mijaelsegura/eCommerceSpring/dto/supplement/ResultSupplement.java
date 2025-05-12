package com.mijaelsegura.eCommerceSpring.dto.supplement;

import com.mijaelsegura.eCommerceSpring.dto.Result;

public class ResultSupplement extends Result {
    private SupplementDto supplement;
    public ResultSupplement(String message, boolean success, String typeError, SupplementDto supplementDto) {
        super(message, success, typeError);
        supplement = supplementDto;
    }

    public ResultSupplement() {

    }

    public SupplementDto getSupplement() {
        return supplement;
    }

    public void setSupplement(SupplementDto supplement) {
        this.supplement = supplement;
    }
}
