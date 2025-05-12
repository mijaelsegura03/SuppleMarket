package com.mijaelsegura.eCommerceSpring.dto.supplement;

import com.mijaelsegura.eCommerceSpring.dto.Result;

import java.util.List;

public class ResultSupplementList extends Result {

    private List<SupplementDto> supplements;

    public ResultSupplementList() {

    }

    public List<SupplementDto> getSupplements() {
        return supplements;
    }

    public void setSupplements(List<SupplementDto> supplements) {
        this.supplements = supplements;
    }
}
