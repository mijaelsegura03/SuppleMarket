package com.mijaelsegura.eCommerceSpring.utils.results;

import com.mijaelsegura.eCommerceSpring.dto.user.ResultUser;
import com.mijaelsegura.eCommerceSpring.dto.user.UserDto;
import org.springframework.stereotype.Component;

@Component
public class GenerateResultUser implements GenerateResult<UserDto> {

    @Override
    public ResultUser getSuccessResult(UserDto user) {
        return new ResultUser("", true, "", user);
    }
}
