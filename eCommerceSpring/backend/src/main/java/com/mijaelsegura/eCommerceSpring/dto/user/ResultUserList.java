package com.mijaelsegura.eCommerceSpring.dto.user;

import com.mijaelsegura.eCommerceSpring.dto.Result;

import java.util.List;

public class ResultUserList extends Result {
    private List<UserDto> users;

    public ResultUserList() {

    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}
