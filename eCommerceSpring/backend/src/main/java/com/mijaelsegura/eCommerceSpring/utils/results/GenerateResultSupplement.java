package com.mijaelsegura.eCommerceSpring.utils.results;

import com.mijaelsegura.eCommerceSpring.dto.Result;
import com.mijaelsegura.eCommerceSpring.dto.supplement.ResultSupplement;
import com.mijaelsegura.eCommerceSpring.dto.supplement.SupplementDto;
import org.springframework.stereotype.Component;

@Component
public class GenerateResultSupplement implements GenerateResult<SupplementDto>{


    @Override
    public ResultSupplement getSuccessResult(SupplementDto supplement) {
        return new ResultSupplement("", true, "", supplement);
    }
}
