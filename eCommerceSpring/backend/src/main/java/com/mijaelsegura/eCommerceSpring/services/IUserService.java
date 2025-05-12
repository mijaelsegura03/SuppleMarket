package com.mijaelsegura.eCommerceSpring.services;

import com.mijaelsegura.eCommerceSpring.dto.user.ResultUser;
import com.mijaelsegura.eCommerceSpring.dto.user.ResultUserList;
import com.mijaelsegura.eCommerceSpring.dto.user.UserDto;
import com.mijaelsegura.eCommerceSpring.models.User;

public interface IUserService {
    ResultUser GetUserByDNI(long dni);
    ResultUserList GetAllUsers();
    ResultUser PutUser(long dni, UserDto userDto);
    ResultUser DeleteUser(long dni);
}
