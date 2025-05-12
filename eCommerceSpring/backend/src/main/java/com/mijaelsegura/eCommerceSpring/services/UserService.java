package com.mijaelsegura.eCommerceSpring.services;


import com.mijaelsegura.eCommerceSpring.dto.user.ResultUser;
import com.mijaelsegura.eCommerceSpring.dto.user.ResultUserList;
import com.mijaelsegura.eCommerceSpring.dto.user.UserDto;
import com.mijaelsegura.eCommerceSpring.exceptions.PropertyValidationException;
import com.mijaelsegura.eCommerceSpring.exceptions.ResourceNotFoundException;
import com.mijaelsegura.eCommerceSpring.models.User;

import com.mijaelsegura.eCommerceSpring.repositories.IUserRepository;
import com.mijaelsegura.eCommerceSpring.utils.results.GenerateResultUser;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final IUserRepository userRepository;
    private final GenerateResultUser resultGenerator;

    public UserService(IUserRepository userRepository, GenerateResultUser resultGenerator) {
        this.userRepository = userRepository;
        this.resultGenerator = resultGenerator;
    }

    @Override
    public ResultUser GetUserByDNI(long dni) {
        Optional<User> userOptional = userRepository.findById(dni);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found any user with DNI: " + dni);
        }
        User user = userOptional.get();
        return resultGenerator.getSuccessResult(new UserDto(user.getDNI(), user.getName(), user.getLastName(), user.getBirthDate(), user.getUsernameString(), user.getPassword()));
    }

    @Override
    public ResultUserList GetAllUsers() {
        ResultUserList res = new ResultUserList();
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            throw new ResourceNotFoundException("Not found any users");
        }
        res.setMessage("");
        res.setSuccess(true);
        res.setUsers(users.stream().map(user ->
                new UserDto(user.getDNI(), user.getName(), user.getLastName(), user.getBirthDate(), user.getUsernameString(), user.getPassword())).toList());
        res.setTypeError("");
        return res;
    }

    @Override
    public ResultUser PutUser(long dni, UserDto userDto) {
        String propertyValidation = userDto.validateAllProperties("PUT");
        if (!Objects.equals(propertyValidation, "")) {
            throw new PropertyValidationException("Could not validate user: " + propertyValidation);
        }
        Optional<User> userOptional = userRepository.findById(dni);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found any user with DNI: " + dni);
        }
        User userFound = userOptional.get();
        userFound.setName(userDto.getName());
        userFound.setLastName(userDto.getLastName());
        userFound.setBirthDate(userDto.getBirthDate());
        userFound.setUsername(userDto.getUsername());
        userRepository.save(userFound);
        userDto.setDNI(dni);
        return resultGenerator.getSuccessResult(userDto);
    }

    @Override
    public ResultUser DeleteUser(long dni) {
        Optional<User> userOptional = userRepository.findById(dni);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("Not found any user with DNI: " + dni);
        }
        User user = userOptional.get();
        userRepository.delete(user);
        return resultGenerator.getSuccessResult(new UserDto(user.getDNI(), user.getName(), user.getLastName(), user.getBirthDate(), user.getUsernameString(), user.getPassword()));
    }


}