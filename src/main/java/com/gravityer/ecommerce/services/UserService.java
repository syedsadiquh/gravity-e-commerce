package com.gravityer.ecommerce.services;

import com.gravityer.ecommerce.dto.UserDto;
import com.gravityer.ecommerce.models.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    public List<User> users;

    public List<UserDto> getUsers() {
        List<UserDto> userDtos = new ArrayList<>();
        for (User user : users) {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUsername(user.getUsername());
            userDto.setEmail(user.getEmail());
            userDtos.add(userDto);
        }
        return userDtos;
    }


}
