package com.mijaelsegura.eCommerceSpring.dto.user;

import com.mijaelsegura.eCommerceSpring.dto.Result;

public class ResultUser extends Result {
    private UserDto user;

    public ResultUser(String message, boolean success, String typeError, UserDto user) {
        super(message, success, typeError);
        this.user = user;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}
