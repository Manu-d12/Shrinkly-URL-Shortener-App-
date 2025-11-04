package com.url.shortener.services;

import com.url.shortener.dtos.UserDto;
import com.url.shortener.models.User;

public interface UserService {
    public UserDto createUser(UserDto userDto);
    public UserDto updateUser(UserDto userDto, String userId);
    public UserDto findUserById(String userId);
    public UserDto findByUsername(String username);
}
