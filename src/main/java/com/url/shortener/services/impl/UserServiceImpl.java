package com.url.shortener.services.impl;

import com.url.shortener.dtos.UserDto;
import com.url.shortener.exceptions.ResourceNotFoundException;
import com.url.shortener.helper.Helper;
import com.url.shortener.models.User;
import com.url.shortener.repositories.UserRepo;
import com.url.shortener.services.UserService;
import jakarta.transaction.TransactionScoped;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class UserServiceImpl implements UserService {


    private Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = this.modelMapper.map(userDto, User.class);
        String userId = Helper.generateUUID();
        user.setId(userId);
        user.setCreatedDate(LocalDateTime.now());
        user.setRole("ROLE_USER");
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User savedUser = this.userRepo.save(user);
        return this.modelMapper.map(savedUser, UserDto.class);
    }


    @Transactional
    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        this.findUserById(userId); // will check if user exists else will throw
        User user = this.modelMapper.map(userDto, User.class);
        user.setId(userId);
        User savedUser = this.userRepo.save(user);
        return this.modelMapper.map(savedUser, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto findUserById(String userId) {
        User user = this.userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", HttpStatus.NOT_FOUND));
        return this.modelMapper.map(user, UserDto.class);
    }

    @Transactional
    @Override
    public UserDto findByUsername(String username) {
       logger.info("USER_NAME: {}", username);
       User user = this.userRepo.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("USER_NOT_FOUND", HttpStatus.NOT_FOUND));
       return this.modelMapper.map(user, UserDto.class);
    }

}
