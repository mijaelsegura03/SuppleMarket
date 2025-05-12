package com.mijaelsegura.eCommerceSpring.utils.results;

import com.mijaelsegura.eCommerceSpring.dto.Result;

public interface GenerateResult<T> {
    Result getSuccessResult(T t);
}
